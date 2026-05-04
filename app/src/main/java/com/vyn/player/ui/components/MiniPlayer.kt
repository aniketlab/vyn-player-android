package com.vyn.player.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.LocalIndication
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vyn.player.ui.screens.player.PlayerViewModel
import com.vyn.player.ui.theme.Surface2
import com.vyn.player.ui.theme.TextSecondary

@Composable
fun MiniPlayer(
    viewModel: PlayerViewModel,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    elevation: Dp = 6.dp,
    cornerRadius: Dp = 16.dp,
    gesturesEnabled: Boolean = true,
) {
    val currentSong by viewModel.currentSong.collectAsStateWithLifecycle()
    val isPlaying by viewModel.isPlaying.collectAsStateWithLifecycle()
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    var totalDrag by remember { mutableStateOf(0f) }
    var isDragging by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.94f else 1f,
        animationSpec = spring(
            dampingRatio = 0.6f,
            stiffness = 400f,
        ),
        label = "pressScale",
    )

    val song = currentSong ?: return

    Surface(
        shape = RoundedCornerShape(cornerRadius),
        tonalElevation = elevation,
        color = Surface2,
        modifier = modifier
            .shadow(
                elevation = 12.dp,
                shape = RoundedCornerShape(cornerRadius),
            )
            .border(
                width = 1.dp,
                color = androidx.compose.ui.graphics.Color(0x1FFFFFFF),
                shape = RoundedCornerShape(28.dp),
            )
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                shadowElevation = elevation.toPx()
            }
            .fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .pointerInput(gesturesEnabled) {
                    if (gesturesEnabled) {
                        detectVerticalDragGestures(
                            onVerticalDrag = { _, dragAmount ->
                                isDragging = true
                                totalDrag += dragAmount
                            },
                            onDragEnd = {
                                if (totalDrag < -100f) {
                                    viewModel.expandPlayer()
                                }
                                totalDrag = 0f
                                isDragging = false
                            },
                        )
                    }
                }
                .clickable(
                    enabled = gesturesEnabled,
                    interactionSource = interactionSource,
                    indication = LocalIndication.current,
                ) {
                    if (!isDragging) {
                        onClick()
                        viewModel.expandPlayer()
                    }
                }
                .padding(horizontal = 14.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp),
                contentAlignment = Alignment.Center,
            ) {
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.18f),
                    modifier = Modifier.fillMaxHeight(),
                ) {}
            }

            Spacer(modifier = Modifier.size(12.dp))

            Column(
                modifier = Modifier.weight(1f),
            ) {
                Text(
                    text = song.title,
                    style = MaterialTheme.typography.titleSmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = song.artistName,
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }

            IconButton(
                onClick = { viewModel.togglePlayPause() },
            ) {
                Icon(
                    imageVector = if (isPlaying) Icons.Rounded.Pause else Icons.Rounded.PlayArrow,
                    contentDescription = if (isPlaying) "Pause" else "Play",
                )
            }
        }
    }
}
