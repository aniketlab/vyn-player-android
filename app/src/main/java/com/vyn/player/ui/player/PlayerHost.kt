package com.vyn.player.ui.player

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.core.tween
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.getValue
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    val navBarPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

    Box(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
    ) {
        if (expansionState == PlayerExpansionState.EXPANDED) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.35f)),
            )
        } else {
            Unit
        }

        AnimatedContent(
            targetState = expansionState,
            transitionSpec = {
                fadeIn(animationSpec = tween(220)) +
                    scaleIn(initialScale = 0.95f) togetherWith
                    fadeOut(animationSpec = tween(180)) +
                    scaleOut(targetScale = 1.05f)
            },
            label = "playerTransition",
        ) { state ->
            if (state == PlayerExpansionState.EXPANDED) {
                PlayerScreen(
                    viewModel = playerViewModel,
                    modifier = Modifier.fillMaxSize(),
                )
            } else {
                MiniPlayer(
                    viewModel = playerViewModel,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = navBarPadding + bottomBarHeight + 12.dp)
                        .padding(horizontal = 16.dp),
                )
            }
        }
    }
}
