package com.vyn.player.domain.usecase

import com.vyn.player.core.player.PlayerController
import com.vyn.player.data.model.Song

class PlaySongUseCase(
    private val playerController: PlayerController,
) {
    operator fun invoke(song: Song) {
        playerController.playSong(song)
    }
}
