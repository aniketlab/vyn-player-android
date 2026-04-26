package com.vyn.player.ui.navigation

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun DynamicBottomBar(
    isPlayerActive: Boolean,
    isScrollingUp: Boolean,
    currentRoute: String?,
    onHomeClick: () -> Unit,
    onDiscoverClick: () -> Unit,
    onLibraryClick: () -> Unit,
    onSearchClick: () -> Unit,
) {
    val showPlayerBar = isPlayerActive && isScrollingUp
    val barHeight = if (isScrollingUp) 64.dp else 72.dp
    val elevation by animateDpAsState(
        targetValue = if (showPlayerBar) 16.dp else 8.dp,
        label = "barElevation",
    )
    val height by animateDpAsState(
        targetValue = if (isScrollingUp) 64.dp else 72.dp,
        label = "barHeight",
    )
    val shape = RoundedCornerShape(if (isScrollingUp) 20.dp else 28.dp)

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .animateContentSize(),
        shape = shape,
        tonalElevation = 8.dp,
        shadowElevation = elevation,
        color = Color(0xFF1A1A1E),
    ) {
        if (isScrollingUp) {
            ExpandedNavBar(
                barHeight = barHeight,
                currentRoute = currentRoute,
                onHomeClick = onHomeClick,
                onDiscoverClick = onDiscoverClick,
                onLibraryClick = onLibraryClick,
                onSearchClick = onSearchClick,
            )
        } else {
            SegmentedNavBar(
                barHeight = barHeight,
                currentRoute = currentRoute,
                onHomeClick = onHomeClick,
                onSearchClick = onSearchClick,
            )
        }
    }
}

@Composable
private fun SegmentedNavBar(
    barHeight: androidx.compose.ui.unit.Dp,
    currentRoute: String?,
    onHomeClick: () -> Unit,
    onSearchClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(barHeight)
            .padding(horizontal = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        NavBarIconItem(
            label = "Home",
            selected = currentRoute == Destinations.HOME,
            onClick = onHomeClick,
            icon = Icons.Filled.Home,
        )

        Spacer(modifier = Modifier.width(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
        )

        Spacer(modifier = Modifier.width(8.dp))

        NavBarIconItem(
            label = "Search",
            selected = currentRoute == Destinations.SEARCH,
            onClick = onSearchClick,
            icon = Icons.Filled.Search,
        )
    }
}

@Composable
private fun ExpandedNavBar(
    barHeight: androidx.compose.ui.unit.Dp,
    currentRoute: String?,
    onHomeClick: () -> Unit,
    onDiscoverClick: () -> Unit,
    onLibraryClick: () -> Unit,
    onSearchClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(barHeight),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        NavBarIconItem(
            label = "Home",
            selected = currentRoute == Destinations.HOME,
            onClick = onHomeClick,
            icon = Icons.Filled.Home,
        )
        NavBarIconItem(
            label = "Discover",
            selected = false,
            onClick = onDiscoverClick,
            icon = Icons.Filled.Star,
        )
        NavBarIconItem(
            label = "Library",
            selected = currentRoute == Destinations.LIBRARY,
            onClick = onLibraryClick,
            icon = Icons.AutoMirrored.Filled.List,
        )
        NavBarIconItem(
            label = "Search",
            selected = currentRoute == Destinations.SEARCH,
            onClick = onSearchClick,
            icon = Icons.Filled.Search,
        )
    }
}

@Composable
private fun NavBarIconItem(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(if (selected) Color.White.copy(alpha = 0.10f) else Color.Transparent)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = MaterialTheme.colorScheme.onSurface,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
        )
    }
}

