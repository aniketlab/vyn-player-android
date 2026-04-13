package com.vyn.player.ui.onboarding.pages

import androidx.compose.runtime.Composable
import com.vyn.player.ui.onboarding.components.OnboardingPage

@Composable
fun DonePage(
    stepLabel: String,
    onFinishClick: () -> Unit,
) {
    OnboardingPage(
        title = "You're all set",
        description = "Your library is ready. Continue to start browsing and playing music.",
        stepLabel = stepLabel,
        buttonText = "Start Listening",
        onButtonClick = onFinishClick,
    )
}
