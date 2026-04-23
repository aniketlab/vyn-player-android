package com.vyn.player.ui.player

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.vyn.player.ui.components.MiniPlayer
import com.vyn.player.ui.screens.player.PlayerScreen
import com.vyn.player.ui.screens.player.PlayerViewModel

@Composable
fun PlayerContainer(
    expanded: Boolean,
    viewModel: PlayerViewModel,
) {
    if (expanded) {
        PlayerScreen(
            viewModel = viewModel,
            onCollapse = {
                viewModel.collapsePlayer()
            },
        )
    } else {
        MiniPlayer(
            viewModel = viewModel,
            onClick = {
                viewModel.expandPlayer()
            },
            modifier = Modifier,
        )
    }
}
