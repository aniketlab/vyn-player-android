package com.vyn.player.ui.onboarding.pages

import androidx.compose.runtime.Composable
import com.vyn.player.ui.onboarding.components.OnboardingPage

@Composable
fun WelcomePage(
    stepLabel: String,
    onNextClick: () -> Unit,
) {
    OnboardingPage(
        title = "Welcome to VYN Player",
        description = "A lightweight music player focused on your local library and quick playback.",
        stepLabel = stepLabel,
        buttonText = "Next",
        onButtonClick = onNextClick,
    )
}
