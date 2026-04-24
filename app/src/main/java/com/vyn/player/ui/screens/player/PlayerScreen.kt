package com.vyn.player.ui.screens.player

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.vyn.player.R
import androidx.compose.ui.res.painterResource
import android.util.Log

@Composable
fun PlayerScreen(
    viewModel: PlayerViewModel,
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

    BackHandler {
        viewModel.collapsePlayer()
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF0A0A0A),
                        Color(0xFF1A1A1A),
                    ),
                ),
            ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .pointerInput(Unit) {
                    detectVerticalDragGestures { _, dragAmount: Float ->
                        if (dragAmount > 20) {
                            viewModel.collapsePlayer()
                        }
                    }
                }
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            val song = playerState.currentSong
            val hasAlbumArt = !song?.albumArtUri.isNullOrBlank()

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = song?.title ?: "No Song",
                    style = MaterialTheme.typography.titleLarge,
                    maxLines = 1,
                )

                Text(
                    text = song?.artistName ?: "Unknown Artist",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray.copy(alpha = 0.7f),
                    maxLines = 1,
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            if (hasAlbumArt) {
                AsyncImage(
                    model = song?.albumArtUri?.also { albumArt ->
                        Log.d("ALBUM_ART", "uri: $albumArt")
                    },
                    contentDescription = null,
                    error = painterResource(R.drawable.default_album),
                    placeholder = painterResource(R.drawable.default_album),
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(20.dp)),
                    contentScale = ContentScale.Crop,
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    Color.DarkGray,
                                    Color(0xFF222222),
                                ),
                            ),
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = Icons.Filled.Home,
                        contentDescription = null,
                        modifier = Modifier.height(80.dp),
                        tint = Color.LightGray,
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Column(
                modifier = Modifier.fillMaxWidth(),
            ) {
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

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(text = formatDuration(currentPosition))
                    Text(text = formatDuration(duration))
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                IconButton(
                    onClick = { viewModel.onEvent(PlayerUiEvent.Previous) },
                    enabled = playerState.currentSong != null,
                ) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Previous",
                    )
                }

                IconButton(
                    onClick = { viewModel.onEvent(PlayerUiEvent.TogglePlayPause) },
                    enabled = playerState.currentSong != null,
                    modifier = Modifier.height(72.dp),
                ) {
                    Icon(
                        imageVector = if (playerState.isPlaying) Icons.Filled.Home else Icons.Filled.PlayArrow,
                        contentDescription = if (playerState.isPlaying) "Pause" else "Play",
                    )
                }

                IconButton(
                    onClick = { viewModel.onEvent(PlayerUiEvent.Next) },
                    enabled = playerState.currentSong != null,
                ) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Next",
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

private fun formatDuration(durationMillis: Long): String {
    val totalSeconds = durationMillis.coerceAtLeast(0L) / 1000L
    val minutes = totalSeconds / 60L
    val seconds = totalSeconds % 60L
    return "%d:%02d".format(minutes, seconds)
}
