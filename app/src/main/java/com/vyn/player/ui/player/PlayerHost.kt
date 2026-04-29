package com.vyn.player.ui.player

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vyn.player.ui.screens.player.PlayerScreen
import com.vyn.player.ui.screens.player.PlayerViewModel
import com.vyn.player.ui.theme.Background

@Composable
fun PlayerHost(
    playerViewModel: PlayerViewModel,
    modifier: Modifier = Modifier,
) {
    val expansionState by playerViewModel.playerExpansionState.collectAsStateWithLifecycle()
    val dimAlpha by animateFloatAsState(
        targetValue = if (expansionState == PlayerExpansionState.EXPANDED) 0.35f else 0f,
        label = "dimAlpha",
    )

    Box(
        modifier = modifier
            .fillMaxSize(),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Background.copy(alpha = dimAlpha)),
        )

        if (expansionState == PlayerExpansionState.EXPANDED) {
            PlayerScreen(
                viewModel = playerViewModel,
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}
