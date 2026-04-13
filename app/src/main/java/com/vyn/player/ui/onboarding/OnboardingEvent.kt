package com.vyn.player.ui.onboarding

sealed interface OnboardingEvent {
    data object NextClicked : OnboardingEvent
    data object PermissionGranted : OnboardingEvent
    data object Finish : OnboardingEvent
}
