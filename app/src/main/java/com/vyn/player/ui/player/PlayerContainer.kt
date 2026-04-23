package com.vyn.player.ui.player

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import com.vyn.player.ui.components.MiniPlayer
import com.vyn.player.ui.screens.player.PlayerScreen
import com.vyn.player.ui.screens.player.PlayerViewModel

@Composable
fun PlayerContainer(
    progress: Float,
    viewModel: PlayerViewModel,
) {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val height = animateDpAsState(
        targetValue = lerp(64.dp, screenHeight, progress),
        animationSpec = tween(300),
        label = "player_container_height",
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .height(height.value)
            .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            .background(MaterialTheme.colorScheme.surface),
    ) {
        MiniPlayer(
            viewModel = viewModel,
            onClick = {
                viewModel.expandPlayer()
            },
            modifier = Modifier
                .alpha(1f - progress)
                .pointerInput(progress) {
                    if (progress < 0.5f) {
                        awaitPointerEventScope {
                            while (true) {
                                awaitPointerEvent()
                            }
                        }
                    }
                },
        )

        if (progress > 0.01f) {
            PlayerScreen(
                viewModel = viewModel,
                onCollapse = {
                    viewModel.collapsePlayer()
                },
                modifier = Modifier.alpha(progress),
            )
        }
    }
}
