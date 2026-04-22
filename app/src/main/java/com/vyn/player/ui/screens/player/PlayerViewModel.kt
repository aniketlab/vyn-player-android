package com.vyn.player.ui.screens.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vyn.player.core.player.PlayerController
import com.vyn.player.data.model.Song
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class PlayerViewModel(
    private val playerController: PlayerController,
) : ViewModel() {
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

    fun onPlaySongs(songs: List<Song>, startIndex: Int) {
        playerController.playSongs(songs, startIndex)
    }

    fun onTogglePlayPause() {
        playerController.togglePlayPause()
    }

    fun togglePlayPause() {
        playerController.togglePlayPause()
    }

    fun onPrevious() {
        playerController.playPrevious()
    }

    fun onNext() {
        playerController.playNext()
    }

    fun onSeek(position: Long) {
        playerController.seekTo(position)
    }
}
