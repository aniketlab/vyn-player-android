package com.vyn.player.ui.navigation

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
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
    val animatedBottom = lerp(90.dp, 16.dp, progress)
    val animatedSide = lerp(16.dp, 88.dp, progress)
    val animatedHeight = lerp(50.dp, 62.dp, progress)
    val cornerRadius = lerp(16.dp, 20.dp, progress)

    Box(modifier = modifier.fillMaxSize()) {
        FloatingNavButton(
            label = "Home",
            selected = currentRoute == Destinations.HOME,
            onClick = onHomeClick,
            icon = Icons.Filled.Home,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 16.dp, bottom = 16.dp)
                .zIndex(1f),
        )

        FloatingNavButton(
            label = "Search",
            selected = currentRoute == Destinations.SEARCH,
            onClick = onSearchClick,
            icon = Icons.Filled.Search,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp, bottom = 16.dp)
                .zIndex(1f),
        )

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = animatedBottom)
                .padding(horizontal = animatedSide)
                .height(animatedHeight)
                .zIndex(2f),
        ) {
            Row(
                modifier = Modifier
                    .matchParentSize()
                    .clip(RoundedCornerShape(cornerRadius))
                    .background(WarmSurface)
                    .alpha(1f - progress)
                    .scale(1f - (0.2f * progress)),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                CenterActionButton(
                    label = "Browser",
                    icon = Icons.Filled.Star,
                    onClick = onDiscoverClick,
                )
                CenterActionButton(
                    label = "Library",
                    icon = Icons.AutoMirrored.Filled.List,
                    onClick = onLibraryClick,
                )
            }

            MiniPlayer(
                viewModel = playerViewModel,
                modifier = Modifier
                    .matchParentSize()
                    .alpha(progress)
                    .scale(0.9f + (0.1f * progress))
                    .clip(RoundedCornerShape(cornerRadius)),
                cornerRadius = cornerRadius,
                gesturesEnabled = isPlayerActive && progress > 0.5f,
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
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        color = WarmSurface,
        tonalElevation = 0.dp,
        shadowElevation = 12.dp,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(WarmSurface)
                .padding(horizontal = 8.dp, vertical = 7.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (selected) MaterialTheme.colorScheme.primary else TextSecondary,
            )
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = if (selected) TextPrimary else TextSecondary,
            )
        }
    }
}
