package com.vyn.player.ui.screens.player

import androidx.media3.common.Player
import com.vyn.player.data.model.Song

data class PlayerUiState(
    val currentSong: Song? = null,
    val isPlaying: Boolean = false,
    val position: Long = 0L,
    val duration: Long = 0L,
    val isBuffering: Boolean = false,
    val playbackState: Int = Player.STATE_IDLE,
    val error: String? = null,
)
