package com.vyn.player.ui.navigation

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vyn.player.ui.components.MiniPlayer
import com.vyn.player.ui.screens.player.PlayerViewModel

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

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(164.dp)
            .padding(horizontal = 16.dp)
            .animateContentSize(),
    ) {
        val floatingBottom by animateDpAsState(
            targetValue = if (isMerged) 16.dp else 90.dp,
            label = "floatingPlayerBottom",
        )
        val floatingAlpha by animateFloatAsState(
            targetValue = if (!isMerged && isPlayerActive) 1f else 0f,
            label = "floatingPlayerAlpha",
        )
        val floatingScale by animateFloatAsState(
            targetValue = if (!isMerged && isPlayerActive) 1f else 0.9f,
            label = "floatingPlayerScale",
        )

        if (!isMerged && isPlayerActive) {
            MiniPlayer(
                viewModel = playerViewModel,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = floatingBottom)
                    .graphicsLayer {
                        alpha = floatingAlpha
                        scaleX = floatingScale
                        scaleY = floatingScale
                    },
            )
        }

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp)
                .align(Alignment.BottomCenter),
            shape = RoundedCornerShape(28.dp),
            tonalElevation = 8.dp,
            color = Color(0xFF1A1A1E),
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
                    modifier = Modifier
                        .weight(1f)
                        .height(62.dp)
                        .animateContentSize(),
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
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.clip(RoundedCornerShape(20.dp)),
        contentAlignment = Alignment.Center,
    ) {
        if (merged && isPlayerActive) {
            val miniPlayerAlpha by animateFloatAsState(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 180),
                label = "miniPlayerAlpha",
            )
            val miniPlayerScale by animateFloatAsState(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 180),
                label = "miniPlayerScale",
            )

            MiniPlayer(
                viewModel = playerViewModel,
                modifier = Modifier
                    .matchParentSize()
                    .graphicsLayer {
                        alpha = miniPlayerAlpha
                        scaleX = miniPlayerScale
                        scaleY = miniPlayerScale
                    },
            )
        } else {
            val centerPillAlpha by animateFloatAsState(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 180),
                label = "centerPillAlpha",
            )
            val centerPillScale by animateFloatAsState(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 180),
                label = "centerPillScale",
            )

            CenterPill(
                onDiscoverClick = onDiscoverClick,
                onLibraryClick = onLibraryClick,
                alpha = centerPillAlpha,
                scale = centerPillScale,
            )
        }
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
        shape = RoundedCornerShape(18.dp),
        color = Color.White.copy(alpha = 0.08f),
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
                color = if (selected) Color.White.copy(alpha = 0.1f) else Color.Transparent,
                shape = CircleShape,
            )
            .padding(horizontal = 8.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center,
    ) {
        androidx.compose.foundation.layout.Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(icon, contentDescription = label)
            Text(label, style = MaterialTheme.typography.bodySmall)
        }
    }
}
