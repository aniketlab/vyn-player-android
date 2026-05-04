package com.vyn.player.ui.navigation

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Explore
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.LibraryMusic
import androidx.compose.material.icons.rounded.Search
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vyn.player.ui.components.MiniPlayer
import com.vyn.player.ui.screens.player.PlayerViewModel

private val StableNavbarShape = RoundedCornerShape(20.dp)
private val StableNavbarColor = Color(0xFF1C1A11)
private val StableNavbarBorder = Color(0x1AF5EEDC)
private val StableInactiveColor = Color(0x99F5EEDC)

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

    val miniPlayerBottom by animateDpAsState(
        targetValue = if (isMerged) 24.dp else 78.dp,
        label = "miniPlayerBottom",
    )
    val miniPlayerScale by animateFloatAsState(
        targetValue = if (isMerged) 1f else 0.95f,
        label = "miniPlayerScale",
    )

    Box(modifier = modifier.fillMaxSize()) {
        Surface(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .fillMaxWidth()
                .height(62.dp),
            shape = StableNavbarShape,
            color = StableNavbarColor,
            border = androidx.compose.foundation.BorderStroke(1.dp, StableNavbarBorder),
            tonalElevation = 0.dp,
            shadowElevation = 12.dp,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                StableNavItem(
                    label = "Home",
                    icon = Icons.Rounded.Home,
                    selected = currentRoute == Destinations.HOME,
                    onClick = onHomeClick,
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    StableNavItem(
                        label = "Browser",
                        icon = Icons.Rounded.Explore,
                        selected = false,
                        onClick = onDiscoverClick,
                    )
                    StableNavItem(
                        label = "Library",
                        icon = Icons.Rounded.LibraryMusic,
                        selected = currentRoute == Destinations.LIBRARY,
                        onClick = onLibraryClick,
                    )
                }

                StableNavItem(
                    label = "Search",
                    icon = Icons.Rounded.Search,
                    selected = currentRoute == Destinations.SEARCH,
                    onClick = onSearchClick,
                )
            }
        }

        if (isPlayerActive) {
            MiniPlayer(
                viewModel = playerViewModel,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = 16.dp)
                    .offset(y = -miniPlayerBottom)
                    .fillMaxWidth()
                    .height(62.dp)
                    .clip(StableNavbarShape)
                    .graphicsLayer {
                        scaleX = miniPlayerScale
                        scaleY = miniPlayerScale
                    },
                elevation = 12.dp,
                cornerRadius = 20.dp,
                gesturesEnabled = true,
            )
        }
    }
}

@Composable
private fun StableNavItem(
    label: String,
    icon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (selected) Color(0xFFE8943A) else StableInactiveColor,
        )
        Text(
            text = label,
            color = if (selected) Color(0xFFF5EEDC) else StableInactiveColor,
            style = MaterialTheme.typography.bodySmall,
        )
    }
}
