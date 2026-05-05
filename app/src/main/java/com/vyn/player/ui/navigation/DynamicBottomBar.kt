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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vyn.player.ui.components.MiniPlayer
import com.vyn.player.ui.screens.player.PlayerViewModel

private val CenterSlotShape = RoundedCornerShape(20.dp)
private val NavbarContainerColor = Color(0xFF1C1A11)
private val NavbarContainerBorder = Color(0x1AF5EEDC)
private val NavbarInactiveColor = Color(0x99F5EEDC)

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
        targetValue = if (isMerged) 0.dp else 90.dp,
        label = "floatingMiniPlayerBottom",
    )
    val centerPillAlpha by animateFloatAsState(
        targetValue = if (isMerged) 0f else 1f,
        label = "centerPillAlpha",
    )
    val centerPillScale by animateFloatAsState(
        targetValue = if (isMerged) 0.8f else 1f,
        label = "centerPillScale",
    )
    val mergedMiniPlayerAlpha by animateFloatAsState(
        targetValue = if (isMerged) 1f else 0f,
        label = "mergedMiniPlayerAlpha",
    )
    val mergedMiniPlayerScale by animateFloatAsState(
        targetValue = if (isMerged) 1f else 0.9f,
        label = "mergedMiniPlayerScale",
    )

    Box(modifier = modifier.fillMaxSize()) {
        if (isPlayerActive && !isMerged) {
            MiniPlayer(
                viewModel = playerViewModel,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = 16.dp)
                    .padding(bottom = floatingBottom)
                    .fillMaxWidth()
                    .height(62.dp),
                elevation = 12.dp,
                cornerRadius = 20.dp,
                gesturesEnabled = true,
            )
        }

        Surface(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .fillMaxWidth()
                .height(62.dp),
            shape = CenterSlotShape,
            color = NavbarContainerColor,
            border = androidx.compose.foundation.BorderStroke(1.dp, NavbarContainerBorder),
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
                NavBarItem(
                    label = "Home",
                    icon = Icons.Rounded.Home,
                    selected = currentRoute == Destinations.HOME,
                    onClick = onHomeClick,
                )

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(62.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0x1AFFFFFF), CenterSlotShape)
                            .border(1.dp, Color(0x1FFFFFFF), CenterSlotShape)
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .alpha(centerPillAlpha)
                            .scale(centerPillScale),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        CenterSlotItem(
                            label = "Discover",
                            icon = Icons.Rounded.Explore,
                            onClick = onDiscoverClick,
                        )
                        CenterSlotItem(
                            label = "Library",
                            icon = Icons.Rounded.LibraryMusic,
                            onClick = onLibraryClick,
                        )
                    }

                    if (isPlayerActive) {
                        MiniPlayer(
                            viewModel = playerViewModel,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(62.dp)
                                .alpha(mergedMiniPlayerAlpha)
                                .scale(mergedMiniPlayerScale),
                            elevation = 12.dp,
                            cornerRadius = 20.dp,
                            gesturesEnabled = isMerged,
                        )
                    }
                }

                NavBarItem(
                    label = "Search",
                    icon = Icons.Rounded.Search,
                    selected = currentRoute == Destinations.SEARCH,
                    onClick = onSearchClick,
                )
            }
        }
    }
}

@Composable
private fun CenterSlotItem(
    label: String,
    icon: ImageVector,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 6.dp),
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
private fun NavBarItem(
    label: String,
    icon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (selected) Color(0xFFE8943A) else NavbarInactiveColor,
        )
        Text(
            text = label,
            color = if (selected) Color(0xFFF5EEDC) else NavbarInactiveColor,
            style = MaterialTheme.typography.bodySmall,
        )
    }
}
