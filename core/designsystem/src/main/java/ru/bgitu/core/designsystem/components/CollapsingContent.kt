package ru.bgitu.core.designsystem.components

import androidx.annotation.RawRes
import androidx.compose.animation.core.LinearEasing
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.constraintlayout.compose.ExperimentalMotionApi
import androidx.constraintlayout.compose.MotionLayout
import ru.bgitu.core.designsystem.util.rememberMotionScene

@OptIn(ExperimentalMotionApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MotionContent(
    scrollBehavior: TopAppBarScrollBehavior,
    @RawRes motionSceneResId: Int,
    pinnedHeight: Dp,
    maxHeight: Dp,
    modifier: Modifier = Modifier,
    params: Map<String, Int>? = null,
    content: @Composable (progress: Float) -> Unit
) {
    val pinnedHeightPx: Float
    val maxHeightPx: Float

    LocalDensity.current.run {
        pinnedHeightPx = pinnedHeight.toPx()
        maxHeightPx = maxHeight.toPx()
    }

    SideEffect {
        if (scrollBehavior.state.heightOffsetLimit != pinnedHeightPx - maxHeightPx) {
            scrollBehavior.state.heightOffsetLimit = pinnedHeightPx - maxHeightPx
        }
    }

    val progress = LinearEasing.transform(scrollBehavior.state.collapsedFraction)

    val appBarDragModifier = if (!scrollBehavior.isPinned) {
        Modifier.draggable(
            orientation = Orientation.Vertical,
            state = rememberDraggableState { delta ->
                scrollBehavior.state.heightOffset += delta
            },
        )
    } else Modifier

    val motionScene = rememberMotionScene(motionSceneResId, params)
    MotionLayout(
        motionScene = motionScene,
        progress = progress,
        modifier = modifier
            .then(appBarDragModifier)
            .fillMaxWidth()
            .height(with(LocalDensity.current) {
                (progress.getValueFromRange(maxHeightPx, pinnedHeightPx)).toDp()
            })
    ) {
        content(progress)
    }
}

private fun Float.getValueFromRange(start: Float, end: Float): Float {
    return (start + (end - start) * this)
}