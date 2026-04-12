package com.vyn.player.domain.usecase

import com.vyn.player.core.player.PlayerController

class TogglePlayUseCase(
    private val playerController: PlayerController,
) {
    operator fun invoke() {
        playerController.togglePlayPause()
    }
}
