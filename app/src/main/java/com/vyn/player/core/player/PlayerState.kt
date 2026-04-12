package com.vyn.player.core.player

import androidx.media3.common.Player
import com.vyn.player.data.model.Song

data class PlayerState(
    val currentSong: Song? = null,
    val queue: List<Song> = emptyList(),
    val currentIndex: Int = -1,
    val isPlaying: Boolean = false,
    val currentPosition: Long = 0L,
    val duration: Long = 0L,
    val isBuffering: Boolean = false,
    val playbackState: Int = Player.STATE_IDLE,
)
