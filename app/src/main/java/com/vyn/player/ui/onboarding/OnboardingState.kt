package com.vyn.player.ui.onboarding

data class OnboardingState(
    val currentPage: Int = 0,
    val isPermissionGranted: Boolean = false,
    val isCompleted: Boolean = false,
)
