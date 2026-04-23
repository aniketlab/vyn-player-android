package com.vyn.player.ui.player

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vyn.player.ui.screens.player.PlayerViewModel

@Composable
fun PlayerHost(
    playerViewModel: PlayerViewModel,
    modifier: Modifier = Modifier,
) {
    val uiState by playerViewModel.playerExpansionState.collectAsStateWithLifecycle()

    BackHandler(enabled = uiState == PlayerExpansionState.EXPANDED) {
        playerViewModel.collapsePlayer()
    }

    val progress by animateFloatAsState(
        targetValue = if (uiState == PlayerExpansionState.EXPANDED) 1f else 0f,
        animationSpec = tween(
            durationMillis = 220,
            easing = FastOutSlowInEasing,
        ),
        label = "player_host_progress",
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 80.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            PlayerContainer(
                progress = progress,
                viewModel = playerViewModel,
            )
        }
    }
}
