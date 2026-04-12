package com.vyn.player.ui.screens.player

import com.vyn.player.data.model.Song

sealed interface PlayerUiEvent {
    data class PlaySong(val song: Song) : PlayerUiEvent
    data class PlaySongs(val songs: List<Song>, val startIndex: Int) : PlayerUiEvent
    data object Previous : PlayerUiEvent
    data object TogglePlayPause : PlayerUiEvent
    data object Next : PlayerUiEvent
    data class Seek(val position: Long) : PlayerUiEvent
}
