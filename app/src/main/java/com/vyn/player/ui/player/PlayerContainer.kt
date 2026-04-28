package com.vyn.player.ui.player

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vyn.player.ui.screens.player.PlayerScreen
import com.vyn.player.ui.screens.player.PlayerViewModel

@Composable
fun PlayerContainer(
    viewModel: PlayerViewModel,
) {
    val state by viewModel.playerExpansionState.collectAsStateWithLifecycle()

    Box(modifier = Modifier.fillMaxSize()) {
        if (state == PlayerExpansionState.EXPANDED) {
        PlayerScreen(
            modifier = Modifier.fillMaxSize(),
            viewModel = viewModel,
        )
        }
    }
}
