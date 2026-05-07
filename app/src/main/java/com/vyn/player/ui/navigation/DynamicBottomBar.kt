package com.vyn.player.ui.navigation

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vyn.player.ui.components.MiniPlayer
import com.vyn.player.ui.screens.player.PlayerViewModel

private val CapsuleShape = RoundedCornerShape(30.dp)
private val CenterPillShape = RoundedCornerShape(36.dp)
private val MiniPlayerShape = RoundedCornerShape(34.dp)
private val FloatingSurfaceColor = Color(0xFF1C1A11)
private val FloatingSurfaceBorder = Color(0x12F5EEDC)
private val FloatingSurfaceOverlay = Color(0x14FFFFFF)
private val FloatingSurfaceOverlayBorder = Color(0x14FFFFFF)
private val ActiveColor = Color(0xFFF5EEDC)
private val ActiveIconColor = Color(0xFFE8943A)
private val InactiveColor = Color(0x99F5EEDC)

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
        label = "floatingMiniPlayerProgress",
    )
    val miniPlayerWidth by animateDpAsState(
        targetValue = lerp(274.dp, 220.dp, progress),
        label = "floatingMiniPlayerWidth",
    )
    val miniPlayerHeight by animateDpAsState(
        targetValue = lerp(56.dp, 54.dp, progress),
        label = "floatingMiniPlayerHeight",
    )
    val miniPlayerOffsetY by animateDpAsState(
        targetValue = lerp((-78).dp, (-70).dp, progress),
        label = "floatingMiniPlayerOffsetY",
    )
    val miniPlayerCorner by animateDpAsState(
        targetValue = lerp(30.dp, 26.dp, progress),
        label = "floatingMiniPlayerCorner",
    )
    val centerPillScale by animateFloatAsState(
        targetValue = if (isMerged) 0.96f else 1f,
        label = "centerPillScale",
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .zIndex(100f),
    ) {
        FloatingCapsule(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 24.dp, bottom = 16.dp)
                .width(88.dp)
                .height(54.dp),
            shape = CapsuleShape,
        ) {
            FloatingSingleNavItem(
                label = "Home",
                icon = Icons.Rounded.Home,
                selected = currentRoute == Destinations.HOME,
                onClick = onHomeClick,
            )
        }

        FloatingCapsule(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp)
                .width(178.dp)
                .height(58.dp)
                .graphicsLayer {
                    scaleX = centerPillScale
                    scaleY = centerPillScale
                },
            shape = CenterPillShape,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp, vertical = 5.dp),
                verticalArrangement = Arrangement.Center,
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 2.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    CenterFloatingNavItem(
                        label = "Discover",
                        icon = Icons.Rounded.Explore,
                        onClick = onDiscoverClick,
                    )
                    CenterFloatingNavItem(
                        label = "Library",
                        icon = Icons.Rounded.LibraryMusic,
                        onClick = onLibraryClick,
                    )
                }
            }
        }

        FloatingCapsule(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 24.dp, bottom = 16.dp)
                .width(88.dp)
                .height(54.dp),
            shape = CapsuleShape,
        ) {
            FloatingSingleNavItem(
                label = "Search",
                icon = Icons.Rounded.Search,
                selected = currentRoute == Destinations.SEARCH,
                onClick = onSearchClick,
            )
        }

        if (isPlayerActive) {
            Surface(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .offset(y = miniPlayerOffsetY)
                    .width(miniPlayerWidth)
                    .height(miniPlayerHeight)
                    .zIndex(101f),
                shape = MiniPlayerShape,
                color = Color.Transparent,
                tonalElevation = 0.dp,
                shadowElevation = 7.dp,
            ) {
                MiniPlayer(
                    viewModel = playerViewModel,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(miniPlayerCorner)),
                    elevation = 6.dp,
                    cornerRadius = miniPlayerCorner,
                    gesturesEnabled = true,
                )
            }
        }
    }
}

@Composable
private fun FloatingCapsule(
    modifier: Modifier,
    shape: RoundedCornerShape,
    content: @Composable () -> Unit,
) {
    Surface(
        modifier = modifier,
        shape = shape,
        color = FloatingSurfaceColor,
        border = BorderStroke(1.dp, FloatingSurfaceBorder),
        tonalElevation = 0.dp,
        shadowElevation = 5.dp,
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(FloatingSurfaceOverlay)
                .border(1.dp, FloatingSurfaceOverlayBorder, shape)
                .padding(horizontal = 6.dp, vertical = 4.dp),
            contentAlignment = Alignment.Center,
        ) {
            content()
        }
    }
}

@Composable
private fun FloatingSingleNavItem(
    label: String,
    icon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .clickable(onClick = onClick)
            .padding(horizontal = 2.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (selected) ActiveIconColor else InactiveColor,
            modifier = Modifier.size(16.dp),
        )
        Text(
            text = label,
            color = if (selected) ActiveColor else InactiveColor,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium,
            modifier = Modifier.padding(start = 4.dp),
        )
    }
}

@Composable
private fun CenterFloatingNavItem(
    label: String,
    icon: ImageVector,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(horizontal = 5.dp, vertical = 3.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = ActiveIconColor,
            modifier = Modifier.size(16.dp),
        )
        Text(
            text = label,
            color = ActiveColor,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Medium,
        )
    }
}
