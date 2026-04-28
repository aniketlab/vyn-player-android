package com.vyn.player.ui.navigation

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.vyn.player.ui.components.MiniPlayer
import com.vyn.player.ui.screens.player.PlayerViewModel

@Composable
fun DynamicBottomBar(
    currentRoute: String?,
    playerViewModel: PlayerViewModel,
    isPlayerActive: Boolean,
    isScrollingUp: Boolean,
    onHomeClick: () -> Unit,
    onDiscoverClick: () -> Unit,
    onLibraryClick: () -> Unit,
    onSearchClick: () -> Unit,
) {
    val isMerged = !isScrollingUp && isPlayerActive
    val widthFraction by animateFloatAsState(
        targetValue = if (isMerged) 0.55f else 1f,
        label = "miniPlayerWidthFraction",
    )
    val offsetY by animateDpAsState(
        targetValue = if (isMerged) 0.dp else (-24).dp,
        label = "miniPlayerOffsetY",
    )
    val elevation by animateDpAsState(
        targetValue = if (isMerged) 4.dp else 16.dp,
        label = "miniPlayerElevation",
    )
    val containerHeight by animateDpAsState(
        targetValue = if (isPlayerActive && !isMerged) 148.dp else 80.dp,
        label = "bottomBarContainerHeight",
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .animateContentSize(),
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(28.dp),
            tonalElevation = 8.dp,
            shadowElevation = 12.dp,
            color = Color(0xFF1A1A1E),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(containerHeight)
                    .padding(horizontal = 12.dp, vertical = 8.dp),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(72.dp)
                        .align(Alignment.BottomCenter),
                    horizontalArrangement = if (isMerged) Arrangement.SpaceBetween else Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    NavItem("Home", currentRoute == Destinations.HOME, onHomeClick, Icons.Filled.Home)

                    if (isMerged) {
                        Spacer(modifier = Modifier.weight(1f))
                    } else {
                        NavItem("Discover", false, onDiscoverClick, Icons.Filled.Star)
                        NavItem("Library", currentRoute == Destinations.LIBRARY, onLibraryClick, Icons.AutoMirrored.Filled.List)
                    }

                    NavItem("Search", currentRoute == Destinations.SEARCH, onSearchClick, Icons.Filled.Search)
                }

                if (isPlayerActive) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(widthFraction)
                            .offset(y = offsetY)
                            .align(if (isMerged) Alignment.Center else Alignment.TopCenter),
                        contentAlignment = Alignment.Center,
                    ) {
                        MiniPlayer(
                            viewModel = playerViewModel,
                            modifier = Modifier,
                            elevation = elevation,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun NavItem(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    icon: ImageVector,
) {
    Column(
        modifier = Modifier
            .clickable(onClick = onClick)
            .background(
                if (selected) Color.White.copy(alpha = 0.1f)
                else Color.Transparent,
                RoundedCornerShape(20.dp)
            )
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(icon, contentDescription = label)
        Text(label, style = MaterialTheme.typography.bodySmall)
    }
}
