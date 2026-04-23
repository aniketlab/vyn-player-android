package com.vyn.player.ui.player

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vyn.player.ui.screens.player.PlayerViewModel

@Composable
fun PlayerHost(
    playerViewModel: PlayerViewModel,
) {
    val uiState by playerViewModel.playerExpansionState.collectAsStateWithLifecycle()

    val progress by animateFloatAsState(
        targetValue = if (uiState == PlayerExpansionState.EXPANDED) 1f else 0f,
        animationSpec = tween(300),
        label = "player_host_progress",
    )

    Box(modifier = Modifier.fillMaxSize()) {
        if (progress > 0f) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f * progress))
                    .clickable {
                        playerViewModel.collapsePlayer()
                    },
            )
        }

        PlayerContainer(
            progress = progress,
            viewModel = playerViewModel,
        )
    }
}
