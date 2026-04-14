package com.vyn.player.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val VynColorScheme = darkColorScheme(
    primary = Primary,
    background = Background,
    surface = Surface,
    secondary = Accent,
    onPrimary = TextPrimary,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    onSecondary = Background,
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
