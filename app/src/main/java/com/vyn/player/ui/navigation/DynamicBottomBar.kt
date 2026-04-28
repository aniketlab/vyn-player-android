package com.vyn.player.ui.navigation

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
    val currentSong by playerViewModel.currentSong.collectAsStateWithLifecycle()
    if (currentSong == null) return

    val height by animateDpAsState(
        targetValue = if (isMerged) 72.dp else 110.dp,
        animationSpec = spring(
            dampingRatio = 0.85f,
            stiffness = Spring.StiffnessMedium,
        ),
        label = "bottomBarHeight",
    )
    val elevation by animateDpAsState(
        targetValue = if (isMerged) 6.dp else 18.dp,
        animationSpec = spring(
            dampingRatio = 0.85f,
            stiffness = Spring.StiffnessMedium,
        ),
        label = "bottomBarElevation",
    )
    val cornerRadius by animateDpAsState(
        targetValue = if (isMerged) 28.dp else 20.dp,
        animationSpec = spring(
            dampingRatio = 0.85f,
            stiffness = Spring.StiffnessMedium,
        ),
        label = "bottomBarCornerRadius",
    )
    val centerWeight by animateFloatAsState(
        targetValue = if (isMerged) 2f else 0.0001f,
        animationSpec = spring(
            dampingRatio = 0.85f,
            stiffness = Spring.StiffnessMedium,
        ),
        label = "centerWeight",
    )
    val sideWeight = 1f

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .animateContentSize(),
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(cornerRadius),
            tonalElevation = 8.dp,
            shadowElevation = 12.dp,
            color = Color(0xFF1A1A1E),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(height)
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier.weight(sideWeight),
                    contentAlignment = Alignment.Center,
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly,
                    ) {
                        NavItem("Home", currentRoute == Destinations.HOME, onHomeClick, Icons.Filled.Home)

                        if (!isMerged) {
                            Spacer(modifier = Modifier.width(8.dp))
                            NavItem("Discover", false, onDiscoverClick, Icons.Filled.Star)
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .weight(centerWeight)
                        .padding(horizontal = 6.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    if (isPlayerActive) {
                        MorphingMiniPlayer(
                            playerViewModel = playerViewModel,
                            elevation = elevation,
                        )
                    }
                }

                Box(
                    modifier = Modifier.weight(sideWeight),
                    contentAlignment = Alignment.Center,
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly,
                    ) {
                        if (!isMerged) {
                            NavItem("Library", currentRoute == Destinations.LIBRARY, onLibraryClick, Icons.AutoMirrored.Filled.List)
                            Spacer(modifier = Modifier.width(8.dp))
                        }

                        NavItem("Search", currentRoute == Destinations.SEARCH, onSearchClick, Icons.Filled.Search)
                    }
                }
            }
        }
    }
}

@Composable
private fun MorphingMiniPlayer(
    playerViewModel: PlayerViewModel,
    modifier: Modifier = Modifier,
    elevation: Dp,
) {
    MiniPlayer(
        viewModel = playerViewModel,
        modifier = modifier,
        elevation = elevation,
    )
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
