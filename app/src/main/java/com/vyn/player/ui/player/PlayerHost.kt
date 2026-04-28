package com.vyn.player.ui.player

import android.util.Log
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.getValue
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vyn.player.ui.components.MiniPlayer
import com.vyn.player.ui.screens.player.PlayerScreen
import com.vyn.player.ui.screens.player.PlayerViewModel

@Composable
fun PlayerHost(
    playerViewModel: PlayerViewModel,
    isScrollingUp: Boolean,
    modifier: Modifier = Modifier,
) {
    val expansionState by playerViewModel.playerExpansionState.collectAsStateWithLifecycle()
    val currentSong by playerViewModel.currentSong.collectAsStateWithLifecycle()
    val dimAlpha by animateFloatAsState(
        targetValue = if (expansionState == PlayerExpansionState.EXPANDED) 0.35f else 0f,
        label = "dimAlpha",
    )
    val targetOffset = if (isScrollingUp) (-60).dp else 0.dp
    val lift by animateDpAsState(
        targetValue = targetOffset.coerceIn((-60).dp, 0.dp),
        animationSpec = spring(dampingRatio = 0.85f),
        label = "miniPlayerLift",
    )
    val scale by animateFloatAsState(
        targetValue = if (isScrollingUp) 0.93f else 1f,
        animationSpec = spring(),
        label = "miniPlayerScale",
    )

    if (currentSong == null && expansionState != PlayerExpansionState.EXPANDED) return

    Log.d("PLAYER_ANIM", "isScrollingUp=$isScrollingUp offset=$lift")

    Box(
        modifier = modifier
            .fillMaxSize(),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = dimAlpha)),
        )

        if (expansionState == PlayerExpansionState.EXPANDED) {
            PlayerScreen(
                viewModel = playerViewModel,
                modifier = Modifier.fillMaxSize(),
            )
        } else {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.BottomCenter,
            ) {
                Box(
                    modifier = Modifier
                        .navigationBarsPadding()
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 94.dp)
                        .fillMaxWidth()
                        .offset(y = lift)
                        .graphicsLayer {
                            scaleX = scale
                            scaleY = scale
                        },
                ) {
                    MiniPlayer(viewModel = playerViewModel)
                }
            }
        }
    }
}
