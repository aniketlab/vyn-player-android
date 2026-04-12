package com.vyn.player.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vyn.player.ui.screens.player.PlayerUiEvent
import com.vyn.player.ui.screens.player.PlayerViewModel

@Composable
fun MiniPlayer(
    viewModel: PlayerViewModel,
    onClick: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val currentSong = uiState.currentSong ?: return

    Surface(
        tonalElevation = 2.dp,
        modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clickable(onClick = onClick),
            ) {
                Text(text = currentSong.title)
            }

            Button(
                onClick = { viewModel.onEvent(PlayerUiEvent.TogglePlayPause) },
            ) {
                Text(text = if (uiState.isPlaying) "Pause" else "Play")
            }

            Button(
                onClick = { viewModel.onEvent(PlayerUiEvent.Next) },
            ) {
                Text(text = "Next")
            }
        }
    }
}
