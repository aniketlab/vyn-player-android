package com.vyn.player.ui.player

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.getValue
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vyn.player.core.ui.UiDimens
import com.vyn.player.ui.components.MiniPlayer
import com.vyn.player.ui.player.PlayerExpansionState
import com.vyn.player.ui.screens.player.PlayerScreen
import com.vyn.player.ui.screens.player.PlayerViewModel

@Composable
fun PlayerHost(
    playerViewModel: PlayerViewModel,
    modifier: Modifier = Modifier,
) {
    val expansionState by playerViewModel.playerExpansionState.collectAsStateWithLifecycle()
    val bottomBarHeight = UiDimens.BottomBarHeight

    Box(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
    ) {
        if (expansionState == PlayerExpansionState.EXPANDED) {
            PlayerScreen(
                viewModel = playerViewModel,
                modifier = Modifier.fillMaxSize(),
            )
        } else {
            MiniPlayer(
                viewModel = playerViewModel,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = bottomBarHeight + 18.dp)
                    .widthIn(max = 600.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            )
        }
    }
}
