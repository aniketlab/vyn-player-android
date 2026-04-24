package com.vyn.player.ui.player

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vyn.player.ui.components.MiniPlayer
import com.vyn.player.ui.screens.player.PlayerScreen
import com.vyn.player.ui.screens.player.PlayerViewModel

@Composable
fun PlayerContainer(
    viewModel: PlayerViewModel,
) {
    val state by viewModel.playerExpansionState.collectAsStateWithLifecycle()

    if (state == PlayerExpansionState.EXPANDED) {
        PlayerScreen(
            modifier = Modifier.fillMaxSize(),
            viewModel = viewModel,
        )
    } else {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter,
        ) {
            MiniPlayer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp),
                viewModel = viewModel,
            )
        }
    }
}
