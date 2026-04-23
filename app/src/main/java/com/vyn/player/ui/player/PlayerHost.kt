package com.vyn.player.ui.player

import androidx.activity.compose.BackHandler
import androidx.compose.material3.Surface
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.foundation.shape.RoundedCornerShape
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

    Box(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .navigationBarsPadding(),
    ) {
        Surface(
            tonalElevation = 8.dp,
            shadowElevation = 12.dp,
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        ) {
            PlayerContainer(
                expanded = uiState == PlayerExpansionState.EXPANDED,
                viewModel = playerViewModel,
            )
        }
    }
}
