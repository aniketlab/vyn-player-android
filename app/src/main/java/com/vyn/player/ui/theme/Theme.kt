package com.vyn.player.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val VynColorScheme = darkColorScheme(
    primary = Primary,
    onPrimary = Background,
    primaryContainer = Surface2,
    onPrimaryContainer = TextPrimary,
    background = Background,
    secondary = Accent,
    onSecondary = Background,
    secondaryContainer = Surface2,
    onSecondaryContainer = TextPrimary,
    surface = Surface,
    surfaceVariant = Surface2,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    onSurfaceVariant = TextSecondary,
    outline = Border,
    outlineVariant = AccentBorder,
    error = Primary,
    onError = Background,
)

@Composable
fun VynPlayerTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = VynColorScheme,
        typography = VynTypography,
        shapes = VynShapes,
        content = content,
    )
}
