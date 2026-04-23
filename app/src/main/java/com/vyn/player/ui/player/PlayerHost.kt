package com.vyn.player.ui.player

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter,
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .navigationBarsPadding(),
        ) {
            PlayerContainer(
                expanded = uiState == PlayerExpansionState.EXPANDED,
                viewModel = playerViewModel,
            )
        }
    }
}
