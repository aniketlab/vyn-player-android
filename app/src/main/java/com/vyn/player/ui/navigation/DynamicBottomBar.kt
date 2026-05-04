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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vyn.player.ui.components.MiniPlayer
import com.vyn.player.ui.screens.player.PlayerViewModel
import com.vyn.player.ui.theme.Surface2

private val FloatingCenterShape = RoundedCornerShape(24.dp)
private val FloatingCenterColor = Color(0xFF252218)
private val FloatingBorderColor = Color(0x1AF5EEDC)
private val FloatingInactiveColor = Color(0x99F5EEDC)

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

    val floatingBottom by animateDpAsState(
        targetValue = if (isMerged) 16.dp else 90.dp,
        label = "floatingMiniPlayerBottom",
    )
    val centerPillAlpha by animateFloatAsState(
        targetValue = if (isMerged) 0f else 1f,
        label = "centerPillAlpha",
    )
    val centerPillScale by animateFloatAsState(
        targetValue = if (isMerged) 0.85f else 1f,
        label = "centerPillScale",
    )

    Box(modifier = modifier.fillMaxSize()) {
        FloatingNavButton(
            label = "Home",
            icon = Icons.Rounded.Home,
            selected = currentRoute == Destinations.HOME,
            onClick = onHomeClick,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 16.dp, bottom = 16.dp),
        )

        FloatingNavButton(
            label = "Search",
            icon = Icons.Rounded.Search,
            selected = currentRoute == Destinations.SEARCH,
            onClick = onSearchClick,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp, bottom = 16.dp),
        )

        Surface(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp),
            shape = FloatingCenterShape,
            color = FloatingCenterColor,
            border = androidx.compose.foundation.BorderStroke(1.dp, FloatingBorderColor),
            tonalElevation = 0.dp,
            shadowElevation = 12.dp,
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .alpha(centerPillAlpha)
                    .scale(centerPillScale),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                CenterAction(
                    label = "Discover",
                    icon = Icons.Rounded.Explore,
                    onClick = onDiscoverClick,
                )
                CenterAction(
                    label = "Library",
                    icon = Icons.Rounded.LibraryMusic,
                    onClick = onLibraryClick,
                )
            }
        }

        if (isPlayerActive) {
            MiniPlayer(
                viewModel = playerViewModel,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = 16.dp)
                    .padding(bottom = floatingBottom)
                    .fillMaxWidth()
                    .height(62.dp)
                    .clip(RoundedCornerShape(20.dp)),
                elevation = 12.dp,
                cornerRadius = 20.dp,
                gesturesEnabled = true,
            )
        }
    }
}

@Composable
private fun CenterAction(
    label: String,
    icon: ImageVector,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 4.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = Color(0xFFE8943A),
        )
        Text(
            text = label,
            color = Color(0xFFF5EEDC),
            style = MaterialTheme.typography.bodySmall,
        )
    }
}

@Composable
private fun FloatingNavButton(
    label: String,
    icon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier
            .clip(CircleShape)
            .clickable(onClick = onClick),
        shape = CircleShape,
        color = Surface2,
        border = androidx.compose.foundation.BorderStroke(1.dp, FloatingBorderColor),
        tonalElevation = 0.dp,
        shadowElevation = 10.dp,
    ) {
        Box(
            modifier = Modifier
                .background(Surface2)
                .padding(horizontal = 16.dp, vertical = 16.dp),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (selected) Color(0xFFE8943A) else FloatingInactiveColor,
            )
        }
    }
}
