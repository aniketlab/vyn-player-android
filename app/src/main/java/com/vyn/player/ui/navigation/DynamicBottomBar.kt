package com.vyn.player.ui.navigation

import androidx.compose.animation.core.animateDpAsState
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
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

    val bottomPadding by animateDpAsState(
        targetValue = if (isMerged) 16.dp else 90.dp,
        label = "miniPlayerBottomPadding",
    )
    val cornerRadius by animateDpAsState(
        targetValue = if (isMerged) 20.dp else 16.dp,
        label = "miniPlayerCornerRadius",
    )
    val playerAlpha by animateFloatAsState(
        targetValue = if (isPlayerActive) 1f else 0f,
        label = "miniPlayerAlpha",
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(164.dp)
            .padding(horizontal = 16.dp),
    ) {
        if (!isMerged && isPlayerActive) {
            MiniPlayer(
                viewModel = playerViewModel,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = 0.dp)
                    .padding(bottom = bottomPadding)
                    .graphicsLayer {
                        alpha = playerAlpha
                    },
                cornerRadius = cornerRadius,
            )
        }

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp)
                .align(Alignment.BottomCenter),
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
                    merged = isMerged,
                    isPlayerActive = isPlayerActive,
                    playerViewModel = playerViewModel,
                    onDiscoverClick = onDiscoverClick,
                    onLibraryClick = onLibraryClick,
                    cornerRadius = cornerRadius,
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
    }
}

@Composable
private fun CenterSlot(
    merged: Boolean,
    isPlayerActive: Boolean,
    playerViewModel: PlayerViewModel,
    onDiscoverClick: () -> Unit,
    onLibraryClick: () -> Unit,
    cornerRadius: Dp,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.clip(RoundedCornerShape(20.dp)),
        contentAlignment = Alignment.Center,
    ) {
        if (merged && isPlayerActive) {
            MiniPlayer(
                viewModel = playerViewModel,
                modifier = Modifier.matchParentSize(),
                cornerRadius = cornerRadius,
            )
        } else {
            CenterPill(
                onDiscoverClick = onDiscoverClick,
                onLibraryClick = onLibraryClick,
            )
        }
    }
}

@Composable
private fun CenterPill(
    onDiscoverClick: () -> Unit,
    onLibraryClick: () -> Unit,
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = Surface2,
        border = BorderStroke(1.dp, Border),
        modifier = Modifier
            .fillMaxWidth()
            .height(62.dp),
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
