package com.vyn.player.ui.navigation

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vyn.player.ui.components.MiniPlayer
import com.vyn.player.ui.screens.player.PlayerViewModel
import com.vyn.player.ui.theme.AccentBorder
import com.vyn.player.ui.theme.Border
import com.vyn.player.ui.theme.Surface as WarmSurface
import com.vyn.player.ui.theme.Surface2
import com.vyn.player.ui.theme.TextPrimary
import com.vyn.player.ui.theme.TextSecondary

@Composable
fun DynamicBottomBar(
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
    val bottomPadding = lerp(90.dp, 16.dp, progress)
    val sidePadding = lerp(16.dp, 88.dp, progress)
    val playerHeight = lerp(50.dp, 62.dp, progress)
    val cornerRadius = lerp(16.dp, 20.dp, progress)
    val centerPillAlpha = 1f - progress
    val centerPillScale = 1f + ((0.8f - 1f) * progress)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(164.dp),
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp)
                .align(Alignment.BottomCenter)
                .zIndex(0f),
            shape = RoundedCornerShape(24.dp),
            tonalElevation = 0.dp,
            shadowElevation = 8.dp,
            color = WarmSurface,
            border = BorderStroke(1.dp, AccentBorder),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(72.dp)
                    .padding(horizontal = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                NavBarIconItem(
                    label = "Home",
                    selected = currentRoute == Destinations.HOME,
                    onClick = onHomeClick,
                    icon = Icons.Filled.Home,
                    modifier = Modifier.size(62.dp),
                )

                CenterSlot(
                    onDiscoverClick = onDiscoverClick,
                    onLibraryClick = onLibraryClick,
                    alpha = centerPillAlpha,
                    scale = centerPillScale,
                    modifier = Modifier
                        .weight(1f)
                        .height(62.dp),
                )

                NavBarIconItem(
                    label = "Search",
                    selected = currentRoute == Destinations.SEARCH,
                    onClick = onSearchClick,
                    icon = Icons.Filled.Search,
                    modifier = Modifier.size(62.dp),
                )
            }
        }

        if (isPlayerActive) {
            MiniPlayer(
                viewModel = playerViewModel,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .zIndex(1f)
                    .padding(bottom = bottomPadding)
                    .padding(horizontal = sidePadding)
                    .height(playerHeight)
                    .clip(RoundedCornerShape(cornerRadius)),
                cornerRadius = cornerRadius,
            )
        }
    }
}

@Composable
private fun CenterSlot(
    onDiscoverClick: () -> Unit,
    onLibraryClick: () -> Unit,
    alpha: Float,
    scale: Float,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.clip(RoundedCornerShape(20.dp)),
        contentAlignment = Alignment.Center,
    ) {
        CenterPill(
            onDiscoverClick = onDiscoverClick,
            onLibraryClick = onLibraryClick,
            alpha = alpha,
            scale = scale,
        )
    }
}

@Composable
private fun CenterPill(
    onDiscoverClick: () -> Unit,
    onLibraryClick: () -> Unit,
    alpha: Float,
    scale: Float,
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = Surface2,
        border = BorderStroke(1.dp, Border),
        modifier = Modifier
            .fillMaxWidth()
            .height(62.dp)
            .graphicsLayer {
                this.alpha = alpha
                scaleX = scale
                scaleY = scale
            },
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            NavBarIconItem("Discover", false, onDiscoverClick, Icons.Filled.Star)
            NavBarIconItem("Library", false, onLibraryClick, Icons.AutoMirrored.Filled.List)
        }
    }
}

@Composable
private fun NavBarIconItem(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    icon: ImageVector,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .clickable(onClick = onClick)
            .background(
                color = if (selected) AccentBorder else androidx.compose.ui.graphics.Color.Transparent,
                shape = CircleShape,
            )
            .padding(horizontal = 8.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center,
    ) {
        androidx.compose.foundation.layout.Column(
            horizontalAlignment = Alignment.CenterHorizontally,
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
