package com.vyn.player.core.player

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.media3.common.MediaItem
import androidx.media3.common.C
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.vyn.player.data.model.Song
import androidx.annotation.VisibleForTesting
import java.util.concurrent.atomic.AtomicBoolean
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class PlayerController(
    context: Context,
) {
    private val player: ExoPlayer = ExoPlayer.Builder(context.applicationContext).build().apply {
        playWhenReady = false
    }
    private val isReleased = AtomicBoolean(false)

    private val controllerScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
    private val mediaItemSongs = mutableMapOf<String, Song>()
    private var queue: List<Song> = emptyList()
    private var currentIndex: Int = -1
    private var lastSyncedState: PlayerState = PlayerState()

    private val _state = MutableStateFlow(PlayerState())
    val state: StateFlow<PlayerState> = _state.asStateFlow()

    private val _position = MutableStateFlow(0L)
    val position: StateFlow<Long> = _position.asStateFlow()

    private val playerListener = object : Player.Listener {
        override fun onIsPlayingChanged(isPlaying: Boolean) {
            syncStateFromPlayer()
        }

        override fun onPlaybackStateChanged(playbackState: Int) {
            if (playbackState == Player.STATE_ENDED) {
                if (player.hasNextMediaItem()) {
                    player.seekToNextMediaItem()
                    player.play()
                } else {
                    player.pause()
                }
                syncStateFromPlayer()
                syncPositionFromPlayer(force = true)
                return
            }

            syncPositionFromPlayer(force = true)
            syncStateFromPlayer()
        }

        override fun onPlayerError(error: PlaybackException) {
            Log.e(TAG, "Player error", error)
            syncPositionFromPlayer(force = true)
            syncStateFromPlayer()
        }

        override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
            syncPositionFromPlayer(force = true)
            syncStateFromPlayer()
        }

        override fun onEvents(player: Player, events: Player.Events) {
            if (
                events.contains(Player.EVENT_POSITION_DISCONTINUITY) ||
                events.contains(Player.EVENT_MEDIA_METADATA_CHANGED) ||
                events.contains(Player.EVENT_TIMELINE_CHANGED) ||
                events.contains(Player.EVENT_PLAYBACK_STATE_CHANGED) ||
                events.contains(Player.EVENT_PLAY_WHEN_READY_CHANGED) ||
                events.contains(Player.EVENT_MEDIA_ITEM_TRANSITION)
            ) {
                syncPositionFromPlayer(force = true)
                syncStateFromPlayer()
            }
        }
    }

    init {
        setupAudioFocus(context.applicationContext)
        player.addListener(playerListener)
        syncStateFromPlayer()
        syncPositionFromPlayer(force = true)
        startPositionUpdates()
    }

    fun playSong(song: Song) {
        Log.d(TAG, "playSong() requested for=${song.title} id=${song.id}")
        playSongs(songs = listOf(song), startIndex = 0)
    }

    fun play(song: Song) {
        playSong(song)
    }

    fun next() {
        playNext()
    }

    fun previous() {
        playPrevious()
    }

    fun playSongs(songs: List<Song>, startIndex: Int) {
        if (isReleased.get()) return

        if (songs.isEmpty()) {
            Log.w(TAG, "Ignoring playSongs() with empty queue")
            return
        }

        val boundedIndex = startIndex.coerceIn(0, songs.lastIndex)
        Log.d(TAG, "playSongs() startIndex=$startIndex boundedIndex=$boundedIndex size=${songs.size}")
        queue = songs.toList()
        currentIndex = boundedIndex

        val mediaItems = queue.mapNotNull(::createMediaItem)
        if (mediaItems.isEmpty()) {
            queue = emptyList()
            currentIndex = -1
            syncStateFromPlayer()
            syncPositionFromPlayer(force = true)
            return
        }

        if (mediaItems.size != queue.size) {
            queue = mediaItems.mapNotNull { mediaItem -> mediaItemSongs[mediaItem.mediaId] }
            currentIndex = boundedIndex.coerceIn(0, queue.lastIndex)
        }

        player.setMediaItems(mediaItems)
        player.prepare()
        player.seekTo(currentIndex, 0L)
        player.play()
        syncStateWithSong(
            song = queue.getOrNull(currentIndex),
            isPlaying = true,
        )
        syncPositionFromPlayer(force = true)
        syncStateFromPlayer()
    }

    fun playNext() {
        if (isReleased.get()) return

        if (currentIndex < queue.lastIndex) {
            currentIndex += 1
            playCurrent()
        }
    }

    fun playPrevious() {
        if (isReleased.get()) return

        if (currentIndex > 0) {
            currentIndex -= 1
            playCurrent()
        }
    }

    fun togglePlayPause() {
        if (isReleased.get()) return

        when {
            player.isPlaying -> player.pause()
            player.playbackState == Player.STATE_IDLE && player.currentMediaItem != null -> {
                player.prepare()
                player.play()
            }
            player.currentMediaItem != null -> player.play()
        }
        syncPositionFromPlayer(force = true)
        syncStateFromPlayer()
    }

    fun seekTo(position: Long) {
        if (isReleased.get() || player.currentMediaItem == null) return

        val boundedPosition = position.coerceAtLeast(0L)
        player.seekTo(boundedPosition)
        syncPositionFromPlayer(force = true)
        syncStateFromPlayer()
    }

    fun release() {
        if (!isReleased.compareAndSet(false, true)) return

        player.removeListener(playerListener)
        controllerScope.cancel()
        player.release()
        mediaItemSongs.clear()
        queue = emptyList()
        currentIndex = -1
        val releasedState = PlayerState()
        lastSyncedState = releasedState
        _state.value = releasedState
        _position.value = 0L
    }

    private fun startPositionUpdates() {
        controllerScope.launch {
            while (isActive) {
                if (isReleased.get()) {
                    break
                }

                if (shouldPollPosition()) {
                    syncPositionFromPlayer(force = false)
                }

                delay(positionUpdateDelayMillis())
            }
        }
    }

    @VisibleForTesting
    internal fun shouldPollPosition(): Boolean {
        return player.currentMediaItem != null && (
            player.isPlaying ||
                player.playbackState == Player.STATE_BUFFERING
        )
    }

    @VisibleForTesting
    internal fun positionUpdateDelayMillis(): Long {
        return if (player.isPlaying) FAST_POSITION_UPDATE_INTERVAL_MS else SLOW_POSITION_UPDATE_INTERVAL_MS
    }

    private fun cacheSong(mediaId: String, song: Song) {
        if (mediaItemSongs.size >= MAX_MEDIA_ITEM_CACHE_SIZE && mediaItemSongs[mediaId] == null) {
            mediaItemSongs.clear()
        }

        mediaItemSongs[mediaId] = song
    }

    private fun createMediaItem(song: Song): MediaItem? {
        val songUri = song.uri.takeIf { it.isNotBlank() }?.let(Uri::parse)
        if (songUri == null || songUri == Uri.EMPTY || songUri.scheme.isNullOrBlank()) {
            Log.w(TAG, "Ignoring playSong() for invalid uri: ${song.uri}")
            return null
        }

        val mediaId = song.id.toString()
        cacheSong(mediaId = mediaId, song = song)
        return MediaItem.fromUri(songUri).buildUpon()
            .setMediaId(mediaId)
            .build()
    }

    @Suppress("UNUSED_PARAMETER")
    private fun setupAudioFocus(context: Context) {
        // TODO: handle audio focus (pause/resume on interruptions)
    }

    private fun syncPositionFromPlayer(force: Boolean) {
        if (isReleased.get()) return

        val playbackEnded = player.playbackState == Player.STATE_ENDED
        val updatedPosition = player.currentPosition
            .takeIf { it != C.TIME_UNSET }
            ?.coerceAtLeast(0L)
            ?: if (playbackEnded) {
                player.duration.takeIf { it != C.TIME_UNSET }?.coerceAtLeast(0L) ?: 0L
            } else {
                0L
            }

        if (!force && _position.value == updatedPosition) {
            return
        }

        _position.value = updatedPosition
    }

    private fun syncStateFromPlayer() {
        if (isReleased.get()) return

        currentIndex = player.currentMediaItemIndex.takeIf { it != C.INDEX_UNSET } ?: currentIndex
        val playbackEnded = player.playbackState == Player.STATE_ENDED
        val updatedState = PlayerState(
            currentSong = player.currentMediaItem?.mediaId?.let(mediaItemSongs::get),
            queue = queue,
            currentIndex = currentIndex,
            isPlaying = if (playbackEnded) false else player.isPlaying,
            currentPosition = _position.value,
            duration = player.duration.takeIf { it != C.TIME_UNSET }?.coerceAtLeast(0L) ?: 0L,
            isBuffering = player.playbackState == Player.STATE_BUFFERING,
            playbackState = player.playbackState,
        )

        if (updatedState == lastSyncedState) {
            return
        }

        lastSyncedState = updatedState
        _state.value = updatedState
    }

    private fun syncStateWithSong(
        song: Song?,
        isPlaying: Boolean,
    ) {
        if (song == null) return

        val updatedState = lastSyncedState.copy(
            currentSong = song,
            isPlaying = isPlaying,
            queue = queue,
            currentIndex = currentIndex,
        )
        lastSyncedState = updatedState
        _state.value = updatedState
    }

    private fun playCurrent() {
        val song = queue.getOrNull(currentIndex) ?: return
        val mediaItem = createMediaItem(song) ?: return

        player.setMediaItem(mediaItem)
        player.prepare()
        player.play()

        syncStateWithSong(song = song, isPlaying = true)
        syncPositionFromPlayer(force = true)
        syncStateFromPlayer()
    }

    private companion object {
        private const val TAG = "PlayerController"
        private const val FAST_POSITION_UPDATE_INTERVAL_MS = 200L
        private const val SLOW_POSITION_UPDATE_INTERVAL_MS = 900L
        private const val MAX_MEDIA_ITEM_CACHE_SIZE = 50
    }
}
