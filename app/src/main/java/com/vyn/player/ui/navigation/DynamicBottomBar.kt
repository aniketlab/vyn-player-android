package com.vyn.player.ui.navigation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Explore
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.LibraryMusic
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vyn.player.ui.screens.player.PlayerViewModel

private val BottomContainerShape = RoundedCornerShape(20.dp)
private val BottomContainerColor = Color(0xFF1C1A11)
private val BottomContainerBorder = Color(0x1AF5EEDC)
private val BottomInactiveColor = Color(0x99F5EEDC)

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
    val isPlaying by playerViewModel.isPlaying.collectAsStateWithLifecycle()

    if (isPlayerActive && currentSong == null) return

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
    ) {
        AnimatedContent(
            targetState = isMerged && isPlayerActive && currentSong != null,
            transitionSpec = {
                (fadeIn(animationSpec = tween(220)) + slideInVertically(animationSpec = tween(220)) { it / 6 }) togetherWith
                    (fadeOut(animationSpec = tween(180)) + slideOutVertically(animationSpec = tween(180)) { it / 6 })
            },
            label = "bottomContainerTransition",
        ) { merged ->
            if (merged) {
                MiniPlayerContainer(
                    title = currentSong?.title.orEmpty(),
                    artist = currentSong?.artistName.orEmpty(),
                    isPlaying = isPlaying,
                    onClick = { playerViewModel.expandPlayer() },
                    onPlayPauseClick = { playerViewModel.togglePlayPause() },
                )
            } else {
                NavbarContainer(
                    currentRoute = currentRoute,
                    onHomeClick = onHomeClick,
                    onDiscoverClick = onDiscoverClick,
                    onLibraryClick = onLibraryClick,
                    onSearchClick = onSearchClick,
                )
            }
        }
    }
}

@Composable
private fun NavbarContainer(
    currentRoute: String?,
    onHomeClick: () -> Unit,
    onDiscoverClick: () -> Unit,
    onLibraryClick: () -> Unit,
    onSearchClick: () -> Unit,
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp),
        shape = BottomContainerShape,
        color = BottomContainerColor,
        border = androidx.compose.foundation.BorderStroke(1.dp, BottomContainerBorder),
        tonalElevation = 0.dp,
        shadowElevation = 12.dp,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            NavActionItem(
                label = "Home",
                icon = Icons.Rounded.Home,
                selected = currentRoute == Destinations.HOME,
                onClick = onHomeClick,
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                NavActionItem(
                    label = "Browser",
                    icon = Icons.Rounded.Explore,
                    selected = false,
                    onClick = onDiscoverClick,
                )
                NavActionItem(
                    label = "Library",
                    icon = Icons.Rounded.LibraryMusic,
                    selected = currentRoute == Destinations.LIBRARY,
                    onClick = onLibraryClick,
                )
            }

            NavActionItem(
                label = "Search",
                icon = Icons.Rounded.Search,
                selected = currentRoute == Destinations.SEARCH,
                onClick = onSearchClick,
            )
        }
    }
}

@Composable
private fun MiniPlayerContainer(
    title: String,
    artist: String,
    isPlaying: Boolean,
    onClick: () -> Unit,
    onPlayPauseClick: () -> Unit,
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .clickable(onClick = onClick),
        shape = BottomContainerShape,
        color = BottomContainerColor,
        border = androidx.compose.foundation.BorderStroke(1.dp, BottomContainerBorder),
        tonalElevation = 0.dp,
        shadowElevation = 12.dp,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.18f)),
            )

            androidx.compose.foundation.layout.Column(
                modifier = Modifier.weight(1f),
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    color = Color(0xFFF5EEDC),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = artist,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0x99F5EEDC),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }

            Icon(
                imageVector = if (isPlaying) androidx.compose.material.icons.Icons.Rounded.Pause else androidx.compose.material.icons.Icons.Rounded.PlayArrow,
                contentDescription = if (isPlaying) "Pause" else "Play",
                tint = Color(0xFFE8943A),
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .clickable(onClick = onPlayPauseClick)
                    .padding(6.dp),
            )
        }
    }
}

@Composable
private fun NavActionItem(
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
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (selected) Color(0xFFE8943A) else BottomInactiveColor,
        )
        Text(
            text = label,
            color = if (selected) Color(0xFFF5EEDC) else BottomInactiveColor,
            style = MaterialTheme.typography.bodySmall,
        )
    }
}
