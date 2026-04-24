package com.vyn.player.ui.screens.player

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vyn.player.core.player.PlayerController
import com.vyn.player.data.model.Song
import com.vyn.player.ui.player.PlayerExpansionState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class PlayerViewModel(
    private val playerController: PlayerController,
) : ViewModel() {
    init {
        Log.d("PLAYER_VM", "INSTANCE CREATED: $this")
    }

    private val _playerExpansionState = MutableStateFlow(PlayerExpansionState.COLLAPSED)
    val playerExpansionState: StateFlow<PlayerExpansionState> = _playerExpansionState

    val currentSong: StateFlow<Song?> = playerController.state
        .map { playerState -> playerState.currentSong }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000L),
            initialValue = null,
        )

    val isPlaying: StateFlow<Boolean> = playerController.state
        .map { playerState -> playerState.isPlaying }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000L),
            initialValue = false,
        )

    val uiState: StateFlow<PlayerUiState> = combine(
        playerController.state,
        playerController.position,
    ) { playerState, position ->
        PlayerUiState(
            currentSong = playerState.currentSong,
            isPlaying = playerState.isPlaying,
            position = position,
            duration = playerState.duration,
            isBuffering = playerState.isBuffering,
            playbackState = playerState.playbackState,
            error = null,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000L),
        initialValue = PlayerUiState(),
    )

    fun onEvent(event: PlayerUiEvent) {
        when (event) {
            is PlayerUiEvent.PlaySong -> onPlaySong(event.song)
            is PlayerUiEvent.PlaySongs -> onPlaySongs(event.songs, event.startIndex)
            PlayerUiEvent.Previous -> onPrevious()
            PlayerUiEvent.TogglePlayPause -> onTogglePlayPause()
            PlayerUiEvent.Next -> onNext()
            is PlayerUiEvent.Seek -> onSeek(event.position)
        }
    }

    fun onPlaySong(song: Song) {
        playerController.playSong(song)
    }

    fun playSong(song: Song) {
        playerController.play(song)
    }

    fun playSongs(songs: List<Song>, startIndex: Int) {
        playerController.playSongs(songs, startIndex)
    }

    fun onPlaySongs(songs: List<Song>, startIndex: Int) {
        playerController.playSongs(songs, startIndex)
    }

    fun onTogglePlayPause() {
        playerController.togglePlayPause()
    }

    fun togglePlayPause() {
        playerController.togglePlayPause()
    }

    fun expandPlayer() {
        Log.d("PLAYER_UI", "Expanded")
        _playerExpansionState.value = PlayerExpansionState.EXPANDED
    }

    fun collapsePlayer() {
        Log.d("PLAYER_UI", "Collapsed")
        _playerExpansionState.value = PlayerExpansionState.COLLAPSED
    }

    fun onPrevious() {
        playerController.playPrevious()
    }

    fun previous() {
        playerController.previous()
    }

    fun onNext() {
        playerController.playNext()
    }

    fun next() {
        playerController.next()
    }

    fun onSeek(position: Long) {
        playerController.seekTo(position)
    }
}
