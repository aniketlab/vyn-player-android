package com.vyn.player.ui.onboarding.pages

import androidx.compose.runtime.Composable
import com.vyn.player.ui.onboarding.components.OnboardingPage
import com.vyn.player.ui.onboarding.components.PermissionCard

@Composable
fun PermissionPage(
    stepLabel: String,
    permissionName: String,
    onRequestPermission: () -> Unit,
) {
    OnboardingPage(
        title = "Allow media access",
        description = "Grant storage permission so VYN Player can discover songs on your device.",
        stepLabel = stepLabel,
        buttonText = "Grant Permission",
        onButtonClick = onRequestPermission,
        content = {
            PermissionCard(permissionName = permissionName)
        },
    )
}
