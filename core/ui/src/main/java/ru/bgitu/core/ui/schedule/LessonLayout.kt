package ru.bgitu.core.ui.schedule

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.util.fastFirstOrNull
import kotlin.math.roundToInt

enum class LessonComponentId {
    START_TIME,
    END_TIME,
    INDICATOR,
    LONG_BREAK,
    SHORT_BREAK,
    LESSON_CARD,
    INDICATOR_GRADIENT
}

data class LessonLayoutParams(
    val indicatorSpaceWidth: Dp,
    val gap: Dp,
    val verticalGap: Dp,
    val firstItem: Boolean
)

@Composable
fun LessonLayout(
    params: LessonLayoutParams,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val density = LocalDensity.current

    val indicatorSpaceWidthPx = with(density) {
        params.indicatorSpaceWidth.toPx().roundToInt()
    }
    val gap = with(density) {
        params.gap.toPx().roundToInt()
    }
    val verticalGap = with(density) {
        params.verticalGap.toPx().roundToInt()
    }

    Layout(
        modifier = modifier,
        content = content,
    ) { measurables, constraints ->

        // Time column
        val (startTime, endTime) = arrayOf(
            measurables.createPlaceable(LessonComponentId.START_TIME),
            measurables.createPlaceable(LessonComponentId.END_TIME)
        ).map { it.measure(constraints) }

        val indicatorOffsetX = startTime.width + gap

        val lessonCardConstraints = constraints.copy(
            maxWidth = constraints.maxWidth - (indicatorOffsetX + gap)
        )
        val lessonCard = measurables.createPlaceable(LessonComponentId.LESSON_CARD)
            .measure(lessonCardConstraints)

        // Indicator
        val indicatorConstraints = constraints.copy(
            maxWidth = indicatorSpaceWidthPx,
        )
        val indicator = measurables.createPlaceable(LessonComponentId.INDICATOR)
            .measure(indicatorConstraints.copy(
                minHeight = lessonCard.height,
                maxHeight = lessonCard.height
            ))
        val longBreak = measurables.createPlaceable(LessonComponentId.LONG_BREAK)
            .measure(constraints)
        val shortBreak = measurables.createOptionalPlaceable(LessonComponentId.SHORT_BREAK)
            ?.measure(constraints)

        val breakPointOffsetY = -(longBreak.height + verticalGap) / 2

        val leftTimeIndicator = measurables.createOptionalPlaceable(LessonComponentId.INDICATOR_GRADIENT)
            ?.measure(indicatorConstraints.copy(
                minWidth = indicatorConstraints.maxWidth,
                maxWidth = indicatorConstraints.maxWidth,
                minHeight = lessonCard.height / 3,
                maxHeight = lessonCard.height / 3
            ))

        layout(constraints.maxWidth, lessonCard.height) {
            startTime.placeRelative(x = 0, y = 0)
            endTime.placeRelative(x = 0, y = startTime.height)

            indicator.placeRelative(
                x = indicatorOffsetX,
                y = 0
            )
            longBreak.placeRelative(
                x = indicatorOffsetX - (longBreak.width / 2),
                y = if (params.firstItem) 0 else breakPointOffsetY
            )
            shortBreak?.placeRelative(
                x = indicatorOffsetX - (shortBreak.width / 2),
                y = if (params.firstItem) lessonCard.height / 2 else (lessonCard.height / 2) - (shortBreak.height / 2)
            )
            lessonCard.placeRelative(
                x = indicatorOffsetX + gap,
                y = 0
            )

            leftTimeIndicator?.placeRelative(
                x = indicatorOffsetX - leftTimeIndicator.width / 2,
                y = indicator.height - leftTimeIndicator.height + 15
            )
        }
    }
}

private fun List<Measurable>.createOptionalPlaceable(layoutId: LessonComponentId): Measurable? {
    return this.fastFirstOrNull { it.layoutId == layoutId }
}

private fun List<Measurable>.createPlaceable(layoutId: LessonComponentId): Measurable {
    return requireNotNull(this.fastFirstOrNull { it.layoutId == layoutId }) {
        "Element with layoutId = $layoutId not found in compose tree"
    }
}