package ru.bgitu.core.designsystem.util

import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout

@Composable
fun TopGoneLayout(
    modifier: Modifier = Modifier,
    top: @Composable () -> Unit,
    content: @Composable () -> Unit
) {
    val imeVisible by rememberImeState()
    var topHeightPx by remember {
        mutableIntStateOf(0)
    }
    val blockPlacementOffsetY by animateIntAsState(
        targetValue = if (imeVisible) -topHeightPx else 0
    )

    Layout(
        content = { content(); top() },
        modifier = modifier
    ) { measurables, constraints ->
        val block = measurables[0].measure(constraints)

        if (constraints.maxHeight > topHeightPx) {
            topHeightPx = constraints.maxHeight - block.height
        }
        val top = measurables.getOrNull(1)
            ?.measure(
                constraints.copy(
                    minHeight = topHeightPx,
                    maxHeight = topHeightPx
                )
            )

        layout(constraints.maxWidth, constraints.maxHeight) {
            top?.placeRelative(
                x = 0,
                y = 0
            )
            block.placeRelative(
                x = 0,
                y = constraints.maxHeight - block.height + blockPlacementOffsetY
            )
        }
    }
}