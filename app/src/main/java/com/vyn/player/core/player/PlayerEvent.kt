package com.vyn.player.core.player

import com.vyn.player.data.model.Song

sealed interface PlayerEvent {
    data class PlaySong(val song: Song) : PlayerEvent
    data class PlaySongs(val songs: List<Song>, val startIndex: Int) : PlayerEvent
    data object TogglePlayPause : PlayerEvent
    data object PlayNext : PlayerEvent
    data object PlayPrevious : PlayerEvent
    data class SeekTo(val position: Long) : PlayerEvent
    data object Release : PlayerEvent
}
