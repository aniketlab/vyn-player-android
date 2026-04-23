package com.vyn.player.ui.screens.player

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun PlayerScreen(
    viewModel: PlayerViewModel,
    onCollapse: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
    val playerState by viewModel.uiState.collectAsStateWithLifecycle()
    val duration = playerState.duration.coerceAtLeast(0L)
    val currentPosition = playerState.position.coerceIn(0L, duration.takeIf { it > 0L } ?: 0L)
    val normalizedPosition = if (duration > 0L) {
        (currentPosition.toFloat() / duration.toFloat()).coerceIn(0f, 1f)
    } else {
        0f
    }
    var sliderValue by remember { mutableFloatStateOf(normalizedPosition) }

    LaunchedEffect(normalizedPosition) {
        sliderValue = normalizedPosition
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures { }
            }
            .pointerInput(Unit) {
                detectVerticalDragGestures { _, dragAmount ->
                    if (dragAmount > 20) {
                        onCollapse?.invoke()
                    }
                }
            },
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Text(text = playerState.currentSong?.title ?: "No Song")

                if (playerState.isBuffering) {
                    Text(text = "Buffering...")
                }

                Text(text = formatDuration(currentPosition))

                Slider(
                    value = sliderValue,
                    onValueChange = { newValue ->
                        sliderValue = newValue
                    },
                    onValueChangeFinished = {
                        val seekPosition = (sliderValue * duration.toFloat()).toLong()
                        viewModel.onEvent(PlayerUiEvent.Seek(seekPosition))
                    },
                    valueRange = 0f..1f,
                    enabled = duration > 0L,
                )

                Text(text = formatDuration(duration))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Button(
                        onClick = { viewModel.onEvent(PlayerUiEvent.Previous) },
                        enabled = playerState.currentSong != null,
                    ) {
                        Text(text = "Previous")
                    }

                    Button(
                        onClick = { viewModel.onEvent(PlayerUiEvent.TogglePlayPause) },
                        enabled = playerState.currentSong != null,
                    ) {
                        Text(text = if (playerState.isPlaying) "Pause" else "Play")
                    }

                    Button(
                        onClick = { viewModel.onEvent(PlayerUiEvent.Next) },
                        enabled = playerState.currentSong != null,
                    ) {
                        Text(text = "Next")
                    }
                }
            }
        }
    }
}

private fun formatDuration(durationMillis: Long): String {
    val totalSeconds = durationMillis.coerceAtLeast(0L) / 1000L
    val minutes = totalSeconds / 60L
    val seconds = totalSeconds % 60L
    return "%d:%02d".format(minutes, seconds)
}
