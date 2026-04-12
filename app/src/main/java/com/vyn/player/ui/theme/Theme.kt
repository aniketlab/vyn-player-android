package com.vyn.player.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(
    primary = VynPrimary,
    background = VynBackground,
    surface = VynSurface,
    onSurface = VynOnSurface,
)

@Composable
fun VynPlayerTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColors,
        typography = VynTypography,
        shapes = VynShapes,
        content = content,
    )
}