package com.vyn.player.ui.navigation

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.Explore
import androidx.compose.material.icons.rounded.QueueMusic
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vyn.player.ui.components.MiniPlayer
import com.vyn.player.ui.screens.player.PlayerViewModel
import com.vyn.player.ui.theme.Surface as WarmSurface
import com.vyn.player.ui.theme.TextPrimary
import com.vyn.player.ui.theme.TextSecondary

private val FloatingBorderColor = androidx.compose.ui.graphics.Color(0x1FFFFFFF)
private val FloatingSurfaceColor = androidx.compose.ui.graphics.Color(0x1AFFFFFF)

@Composable
fun DynamicBottomBar(
    modifier: Modifier = Modifier,
    currentRoute: String?,
    playerViewModel: PlayerViewModel,
    isPlayerActive: Boolean,
    isMerged: Boolean,
    onHomeClick: () -> Unit,
    onDiscoverClick: () -> Unit,
    onLibraryClick: () -> Unit,
    onSearchClick: () -> Unit,
) {
    val currentSong by playerViewModel.currentSong.collectAsStateWithLifecycle()
    if (isPlayerActive && currentSong == null) return

    val progress by animateFloatAsState(
        targetValue = if (isMerged) 1f else 0f,
        label = "miniPlayerMergeProgress",
    )
    val animatedBottom = lerp(92.dp, 16.dp, progress)
    val animatedSide = lerp(16.dp, 88.dp, progress)
    val animatedHeight = lerp(50.dp, 62.dp, progress)
    val cornerRadius = lerp(16.dp, 20.dp, progress)

    Box(modifier = modifier.fillMaxSize()) {
        MiniPlayer(
            viewModel = playerViewModel,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = animatedSide)
                .offset(y = -animatedBottom)
                .height(animatedHeight)
                .fillMaxWidth()
                .zIndex(10f)
                .clip(RoundedCornerShape(cornerRadius)),
            cornerRadius = cornerRadius,
            gesturesEnabled = isPlayerActive,
        )

        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
                .fillMaxWidth()
                .zIndex(1f),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            FloatingNavButton(
                label = "Home",
                selected = currentRoute == Destinations.HOME,
                onClick = onHomeClick,
                icon = Icons.Filled.Home,
            )

            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(62.dp),
                contentAlignment = Alignment.Center,
            ) {
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(28.dp))
                        .background(FloatingSurfaceColor)
                        .border(
                            width = 1.dp,
                            color = FloatingBorderColor,
                            shape = RoundedCornerShape(28.dp),
                        )
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .graphicsLayer {
                            alpha = 1f - progress
                            scaleX = 1f - (0.2f * progress)
                            scaleY = 1f - (0.2f * progress)
                        },
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    CenterActionButton(
                        label = "Browser",
                        icon = Icons.Rounded.Explore,
                        onClick = onDiscoverClick,
                    )
                    CenterActionButton(
                        label = "Library",
                        icon = Icons.Rounded.QueueMusic,
                        onClick = onLibraryClick,
                    )
                }
            }

            FloatingNavButton(
                label = "Search",
                selected = currentRoute == Destinations.SEARCH,
                onClick = onSearchClick,
                icon = Icons.Filled.Search,
            )
        }
    }
}

@Composable
private fun CenterActionButton(
    label: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = MaterialTheme.colorScheme.primary,
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = TextPrimary,
        )
    }
}

@Composable
private fun FloatingNavButton(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    icon: ImageVector,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier
            .size(64.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(FloatingSurfaceColor)
            .border(
                width = 1.dp,
                color = FloatingBorderColor,
                shape = RoundedCornerShape(20.dp),
            )
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        color = FloatingSurfaceColor,
        tonalElevation = 0.dp,
        shadowElevation = 12.dp,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(FloatingSurfaceColor)
                .padding(horizontal = 8.dp, vertical = 7.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (selected) MaterialTheme.colorScheme.primary else androidx.compose.ui.graphics.Color.White.copy(alpha = 0.6f),
            )
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = if (selected) TextPrimary else androidx.compose.ui.graphics.Color.White.copy(alpha = 0.6f),
            )
        }
    }
}
