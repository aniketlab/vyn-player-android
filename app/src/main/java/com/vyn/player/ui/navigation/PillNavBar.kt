package com.vyn.player.ui.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

private object PillNavBarDefaults {
    val ContainerShape = RoundedCornerShape(56.dp)
    val ItemShape = RoundedCornerShape(50.dp)
    val ContainerColor = Color(0xE61A1A1A)
    val BorderColor = Color.White.copy(alpha = 0.10f)
    val SelectedItemColor = Color.White.copy(alpha = 0.12f)
    val SelectedContentColor = Color.White
    const val UnselectedAlpha = 0.68f
    val ContainerWidthFraction = 0.88f
    val ContainerHeight = 64.dp
    val OuterHorizontalPadding = 14.dp
    val InnerHorizontalPadding = 8.dp
    val ItemHorizontalPadding = 16.dp
    val SelectedIconSize = 24.dp
    val UnselectedIconSize = 22.dp
}

@Composable
private fun Modifier.pillShadow(
    shape: Shape,
): Modifier =
    this.shadow(
        elevation = 8.dp,
        shape = shape,
        clip = false,
        ambientColor = Color.Black.copy(alpha = 0.32f),
        spotColor = Color.Black.copy(alpha = 0.40f),
    )

data class PillNavItem(
    val route: String,
    val label: String,
    val icon: ImageVector,
)

@Composable
fun PillNavBar(
    items: List<PillNavItem>,
    currentRoute: String?,
    onItemClick: (PillNavItem) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth(PillNavBarDefaults.ContainerWidthFraction)
            .height(PillNavBarDefaults.ContainerHeight)
            .pillShadow(PillNavBarDefaults.ContainerShape)
            .clip(PillNavBarDefaults.ContainerShape)
            .background(PillNavBarDefaults.ContainerColor)
            .border(
                width = 1.dp,
                color = PillNavBarDefaults.BorderColor,
                shape = PillNavBarDefaults.ContainerShape,
            )
            .padding(
                horizontal = PillNavBarDefaults.OuterHorizontalPadding,
                vertical = 8.dp,
            ),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        items.forEachIndexed { index, item ->
            val selected = currentRoute == item.route
            NavItem(
                item = item,
                selected = selected,
                onClick = { onItemClick(item) },
                modifier = Modifier,
            )

            if (index != items.lastIndex) {
                Spacer(modifier = Modifier.width(6.dp))
            }
        }
    }
}

@Composable
fun NavItem(
    item: PillNavItem,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scale = animateFloatAsState(
        targetValue = if (selected) 1f else 0.96f,
        animationSpec = spring(
            dampingRatio = 0.82f,
            stiffness = 520f,
        ),
        label = "nav_item_scale",
    )

    val iconScale = animateFloatAsState(
        targetValue = if (selected) 1.08f else 1f,
        animationSpec = tween(
            durationMillis = 220,
            easing = FastOutSlowInEasing,
        ),
        label = "nav_icon_scale",
    )

    Row(
        modifier = modifier
            .padding(horizontal = 4.dp)
            .scale(scale.value)
            .clip(PillNavBarDefaults.ItemShape)
            .background(
                color = if (selected) {
                    PillNavBarDefaults.SelectedItemColor
                } else {
                    Color.Transparent
                },
                shape = PillNavBarDefaults.ItemShape,
            )
            .clickable(onClick = onClick)
            .defaultMinSize(minHeight = 44.dp)
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = 260,
                    easing = FastOutSlowInEasing,
                ),
            )
            .padding(
                horizontal = PillNavBarDefaults.ItemHorizontalPadding,
                vertical = 10.dp,
            ),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = item.icon,
            contentDescription = item.label,
            tint = PillNavBarDefaults.SelectedContentColor,
            modifier = Modifier
                .size(
                    if (selected) {
                        PillNavBarDefaults.SelectedIconSize
                    } else {
                        PillNavBarDefaults.UnselectedIconSize
                    },
                )
                .scale(iconScale.value)
                .alpha(if (selected) 1f else PillNavBarDefaults.UnselectedAlpha),
        )

        AnimatedVisibility(
            visible = selected,
            enter = fadeIn(animationSpec = tween(180)) + expandHorizontally(animationSpec = tween(220)),
            exit = fadeOut(animationSpec = tween(120)) + shrinkHorizontally(animationSpec = tween(180)),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Spacer(modifier = Modifier.size(PillNavBarDefaults.InnerHorizontalPadding))
                Text(
                    text = item.label,
                    color = PillNavBarDefaults.SelectedContentColor,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    modifier = Modifier.padding(end = 4.dp),
                )
            }
        }
    }
}
