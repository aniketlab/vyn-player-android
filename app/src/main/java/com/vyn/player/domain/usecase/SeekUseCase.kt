package com.vyn.player.domain.usecase

import com.vyn.player.core.player.PlayerController

class SeekUseCase(
    private val playerController: PlayerController,
) {
    operator fun invoke(positionMillis: Long) {
        playerController.seekTo(positionMillis)
    }
}
