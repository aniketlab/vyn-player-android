package com.vyn.player.ui.navigation

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
import com.vyn.player.ui.theme.AccentBorder
import com.vyn.player.ui.theme.Background
import com.vyn.player.ui.theme.Border
import com.vyn.player.ui.theme.Surface
import com.vyn.player.ui.theme.TextPrimary

private object PillNavBarDefaults {
    val ContainerShape = RoundedCornerShape(18.dp)
    val ItemShape = RoundedCornerShape(50.dp)
    val ContainerColor = Surface
    val BorderColor = Border
    val SelectedItemColor = AccentBorder
    val SelectedContentColor = TextPrimary
    const val UnselectedAlpha = 0.68f
    val ContainerWidthFraction = 0.88f
    val ContainerHeight = 72.dp
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
        ambientColor = Background.copy(alpha = 0.32f),
        spotColor = Background.copy(alpha = 0.40f),
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
            .wrapContentWidth()
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
                Spacer(modifier = Modifier.width(10.dp))
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
        animationSpec = tween(
            durationMillis = 120,
        ),
        label = "nav_item_scale",
    )

    val interactionSource = remember { MutableInteractionSource() }

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
            .clickable(
                indication = null,
                interactionSource = interactionSource,
                onClick = onClick,
            )
            .defaultMinSize(minHeight = 44.dp)
            .padding(
                horizontal = 18.dp,
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
                .alpha(if (selected) 1f else 0.7f),
        )

        if (selected) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = item.label,
                    color = PillNavBarDefaults.SelectedContentColor,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                )
            }
        }
    }
}
