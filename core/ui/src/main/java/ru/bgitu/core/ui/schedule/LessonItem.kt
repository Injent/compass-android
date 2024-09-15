package ru.bgitu.core.ui.schedule

import android.icu.text.RelativeDateTimeFormatter
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.atDate
import ru.bgitu.core.common.CommonStrings
import ru.bgitu.core.common.DateTimeUtil
import ru.bgitu.core.common.LessonDataUtils
import ru.bgitu.core.designsystem.components.AppCard
import ru.bgitu.core.designsystem.icon.AppIcons
import ru.bgitu.core.designsystem.theme.AppTheme
import ru.bgitu.core.designsystem.theme.LocalAppRipple
import ru.bgitu.core.designsystem.util.SoftwareLayer
import ru.bgitu.core.designsystem.util.shimmer
import ru.bgitu.core.ui.R
import ru.bgitu.core.ui.model.UiLesson
import ru.bgitu.core.ui.onClick
import java.time.temporal.ChronoUnit
import kotlin.math.absoluteValue

@Composable
fun LoadingLessonItems(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.s)
    ) {
        repeat(4) {
            Spacer(
                Modifier
                    .fillMaxWidth()
                    .height(72.dp)
                    .shimmer(shape = AppTheme.shapes.default)
            )
        }
    }
}

@Composable
fun LessonItem(
    lesson: UiLesson,
    now: LocalDateTime,
    onClick: () -> Unit,
    minTimeWidth: Dp,
    modifier: Modifier = Modifier,
    expanded: Boolean = false,
) {
    val layoutParams = remember {
        LessonLayoutParams(
            indicatorSpaceWidth = 12.dp,
            gap = 24.dp,
            verticalGap = 8.dp,
            firstItem = lesson.isFirst
        )
    }
    LessonLayout(
        modifier = modifier,
        params = layoutParams
    ) {
        val startTime = remember(lesson.startTime) {
            DateTimeUtil.formatTime(lesson.startTime)
        }
        val endTime = remember(lesson.endTime) {
            DateTimeUtil.formatTime(lesson.endTime)
        }
        val breakInterval = rememberBreakTimeInterval(
            now = now,
            breakTime = lesson.breakTime
        )
        val classInterval = rememberLessonTimeInterval(
            now = now,
            lessonStartTime = if (lesson.passed) {
                lesson.endTime.atDate(lesson.date)
            } else {
                lesson.startTime.atDate(lesson.date)
            },
            passed = lesson.passed
        )

        Text(
            text = startTime,
            style = AppTheme.typography.headline2,
            color = AppTheme.colorScheme.foreground1,
            maxLines = 1,
            overflow = TextOverflow.Visible,
            modifier = Modifier
                .width(minTimeWidth)
                .layoutId(LessonComponentId.START_TIME)
        )
        Text(
            text = endTime,
            color = AppTheme.colorScheme.foreground3,
            style = AppTheme.typography.caption1,
            maxLines = 1,
            modifier = Modifier.layoutId(LessonComponentId.END_TIME)
        )
        LessonIndicator(
            options = LessonIndicatorOptions(
                defaultStrokeWidth = 2.4.dp,
                dashInterval = 4.dp,
                dashWidth = 3.dp,
                passed = lesson.passed,
                highlighted = lesson.highlighted,
                isFirst = lesson.isFirst,
            ),
            modifier = Modifier.layoutId(LessonComponentId.INDICATOR)
        )
        LessonBreakPoint(
            smallPoint = false,
            enabled = lesson.passed || lesson.highlighted,
            animated = lesson.highlighted,
            highlighted = lesson.passed || lesson.highlighted,
            modifier = Modifier.layoutId(LessonComponentId.LONG_BREAK),
            label = classInterval
        )
        if (lesson.date == now.date && !lesson.passed) {
            LessonBreakPoint(
                smallPoint = true,
                highlighted = lesson.highlighted,
                label = breakInterval,
                modifier = Modifier.layoutId(LessonComponentId.SHORT_BREAK)
            )
        }

        AnimatedContent(
            targetState = expanded,
            label = "expand_state",
            transitionSpec = {
                expandVertically { it } togetherWith shrinkVertically { it }
            },
            modifier = Modifier
                .layoutId(LessonComponentId.LESSON_CARD)
                .clip(AppTheme.shapes.default)
                .clickable(
                    onClick = onClick,
                    interactionSource = remember { MutableInteractionSource() },
                    indication = rememberRipple(
                        bounded = true,
                        color = LocalAppRipple.current.background1
                    ),
                )
        ) { isExpanded ->
            LessonCard(
                lesson = lesson,
                expanded = isExpanded,
                modifier = Modifier
            )
        }

        if (lesson.isLast) {
            Box(
                modifier = Modifier
                    .layoutId(LessonComponentId.INDICATOR_GRADIENT)
                    .background(
                        brush = Brush.verticalGradient(
                            listOf(Color.Transparent, AppTheme.colorScheme.background2),
                        )
                    )
            )
        }
    }
}

@Composable
private fun LessonCard(
    lesson: UiLesson,
    expanded: Boolean,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    AppCard(
        contentPadding = PaddingValues(AppTheme.spacing.m),
        color = AppTheme.colorScheme.backgroundTouchable,
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = lesson.name,
                    style = AppTheme.typography.body,
                    color = AppTheme.colorScheme.foreground1,
                    modifier = Modifier.fillMaxWidth(0.75f),
                    maxLines = if (expanded) 5 else 2,
                    overflow = TextOverflow.Ellipsis
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = stringResource(
                            if (lesson.isLecture) {
                                CommonStrings.lecture_abbriviation
                            } else CommonStrings.practice_abbriviation
                        ),
                        style = AppTheme.typography.callout,
                        color = AppTheme.colorScheme.foreground2,
                        lineHeight = AppTheme.typography.callout.fontSize,
                        modifier = Modifier.padding(end = 2.dp),
                        fontWeight = FontWeight.Medium
                    )
                    Icon(
                        painter = painterResource(
                            if (lesson.isLecture) AppIcons.BookCover else AppIcons.Flask
                        ),
                        contentDescription = null,
                        modifier = Modifier.size(12.dp),
                        tint = AppTheme.colorScheme.foreground2
                    )
                }
            }
            lesson.teacher.takeIf { it.isNotEmpty() }?.let {
                Text(
                    text = lesson.teacher,
                    color = AppTheme.colorScheme.foreground2,
                    style = AppTheme.typography.footnote
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.xs),
                modifier = Modifier.padding(top = AppTheme.spacing.xs)
            ) {
                Icon(
                    imageVector = Icons.Rounded.LocationOn,
                    contentDescription = null,
                    tint = AppTheme.colorScheme.foreground2,
                    modifier = Modifier.size(14.dp)
                )
                val location: String = remember(lesson) {
                    LessonDataUtils.provideLocation(
                        context = context,
                        building = lesson.building,
                        classroom = lesson.classroom,
                        style = LessonDataUtils.DisplayStyle.DEFAULT
                    )
                }
                Text(
                    text = location,
                    fontSize = 14.sp,
                    style = AppTheme.typography.body,
                    color = AppTheme.colorScheme.foreground2,
                    maxLines = if (expanded) 5 else 1
                )
            }
        }
    }
}

@Stable
data class LessonIndicatorOptions(
    val defaultStrokeWidth: Dp,
    val dashInterval: Dp,
    val dashWidth: Dp,
    val passed: Boolean,
    val highlighted: Boolean,
    val isFirst: Boolean,
)

@Composable
private fun LessonIndicator(
    options: LessonIndicatorOptions,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current
    val dashInterval = remember(density.density, options.dashInterval) {
        with(density) { options.dashInterval.toPx() }
    }
    val dashWidth = remember(density.density, options.dashWidth) {
        with(density) { options.dashWidth.toPx() }
    }
    val strokeWidth = remember(density.density, options.defaultStrokeWidth) {
        with(density) { options.defaultStrokeWidth.toPx() }
    }

    val highlightedLineColor = AppTheme.colorScheme.foregroundAccent
    val lineColor = if (options.passed) highlightedLineColor else AppTheme.colorScheme.foreground4

    val infiniteTransition = rememberInfiniteTransition(label = "animated_dash")

    val startOffsetY by if (options.highlighted) {
        infiniteTransition.animateFloat(
            initialValue = -((dashWidth + dashInterval) / 2) * 2f,
            targetValue = ((dashWidth + dashInterval) / 2) * 2f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 700, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            ),
            label = "animated_dash"
        )
    } else remember { mutableFloatStateOf(0f) }

    val dashEffect = remember {
        PathEffect.dashPathEffect(floatArrayOf(dashWidth, dashInterval), 0f)
    }

    SoftwareLayer(modifier) {
        Canvas(
            modifier = Modifier.fillMaxSize(),
            onDraw = {
                val offsetX = center.x

                if (!options.highlighted) {
                    drawLine(
                        color = lineColor,
                        start = Offset(x = offsetX, y = if (options.isFirst) 10f else 0f),
                        end = Offset(x = offsetX, y = size.height),
                        strokeWidth = strokeWidth,
                        pathEffect = dashEffect,
                        cap = StrokeCap.Round
                    )
                } else {
                    val path = Path().apply {
                        addRect(
                            Rect(
                                left = -50f,
                                top = 0f,
                                right = 50f,
                                bottom = size.height
                            )
                        )
                    }

                    clipPath(
                        path = path
                    ) {
                        drawLine(
                            color = highlightedLineColor,
                            start = Offset(
                                offsetX,
                                startOffsetY
                            ),
                            end = Offset(offsetX, size.height),
                            pathEffect = dashEffect,
                            strokeWidth = strokeWidth,
                            cap = StrokeCap.Round
                        )
                    }
                }
            }
        )
    }
}

@Composable
private fun LessonBreakPoint(
    smallPoint: Boolean,
    modifier: Modifier = Modifier,
    animated: Boolean = false,
    enabled: Boolean = true,
    highlighted: Boolean = false,
    label: String? = null
) {
    val density = LocalDensity.current
    var isPopupVisible by remember { mutableStateOf(false) }
    val pointSize = if (smallPoint) 12.dp else 14.dp
    val pointBorderWidth = if (smallPoint) 2.5.dp else 2.8.dp

    val pointFillColor = if (animated) {
        val infiniteTransition = rememberInfiniteTransition(label = "")
        infiniteTransition.animateColor(
            initialValue = Color.White,
            targetValue = AppTheme.colorScheme.background2,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 1000,
                    easing = FastOutSlowInEasing,
                    delayMillis = 300
                ),
                repeatMode = RepeatMode.Reverse
            ),
            label = "circle progress animation alpha"
        ).value
    } else Color.White

    val circleColor = if (enabled || highlighted) {
        pointFillColor
    } else AppTheme.colorScheme.foreground3

    val strokeColor = if (enabled && highlighted) {
        AppTheme.colorScheme.foreground
    } else AppTheme.colorScheme.foreground3

    Box(
        modifier = modifier
            .size(if (enabled || highlighted) pointSize else 10.dp)
            .drawBehind {
                drawCircle(
                    color = circleColor,
                    radius = size.minDimension / 2f - pointBorderWidth.toPx() / 3f
                )

                if (enabled || highlighted) {
                    drawCircle(
                        color = strokeColor,
                        radius = size.minDimension / 2f - pointBorderWidth.toPx() / 2f,
                        style = Stroke(
                            width = pointBorderWidth.toPx()
                        )
                    )
                }
            }
            .onClick { isPopupVisible = !isPopupVisible }
    ) {
        if (label != null && isPopupVisible && enabled) {
            val popOffsetX = with(density) {
                -pointSize.roundToPx() / 2
            }

            LessonBreakPointPopup(
                label = label,
                onDismissRequest = {
                    isPopupVisible = false
                },
                offset = IntOffset(
                    x = popOffsetX,
                    y = 0
                ),
                startSize = pointSize - pointBorderWidth,
                endSize = 12.dp
            )
        }
    }
}

@Composable
private fun LessonBreakPointPopup(
    label: String,
    onDismissRequest: () -> Unit,
    startSize: Dp,
    endSize: Dp,
    modifier: Modifier = Modifier,
    offset: IntOffset = IntOffset(x = 0, y = 0)
) {
    Popup(
        alignment = Alignment.CenterStart,
        offset = offset,
        onDismissRequest = onDismissRequest
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.s),
            modifier = modifier
                .background(
                    color = AppTheme.colorScheme.backgroundBrand,
                    shape = AppTheme.shapes.default
                )
                .padding(
                    horizontal = AppTheme.spacing.s,
                    vertical = AppTheme.spacing.xs
                )
        ) {
            var playedAnim by remember { mutableStateOf(false) }
            LaunchedEffect(Unit) {
                playedAnim = true
            }

            val pointSize by animateDpAsState(
                targetValue = if (playedAnim) endSize else startSize,
                animationSpec = tween()
            )

            Box(
                Modifier.size(endSize)
            ) {
                Spacer(
                    modifier = Modifier
                        .size(pointSize)
                        .align(Alignment.Center)
                        .background(Color.White, CircleShape)
                )
            }
            Text(
                text = label,
                color = AppTheme.colorScheme.foregroundOnBrand,
                style = AppTheme.typography.callout
                    .copy(lineHeight = 14.sp),
                modifier = Modifier.widthIn(max = 300.dp)
            )
        }
    }
}

@Composable
private fun rememberBreakTimeInterval(
    now: LocalDateTime,
    breakTime: LocalDateTime
): String {
    val context = LocalContext.current

    return remember(now, breakTime) {
        val breakIn = DateTimeUtil.difference(
            start = now,
            end = breakTime,
            chronoUnit = ChronoUnit.MINUTES
        )

        if (breakIn >= 60) {
            return@remember context.getString(R.string.break_at) + " " + DateTimeUtil.formatTime(breakTime.time)
        }

        if (breakIn > 0) {
            context.getString(R.string.break_in) + " " + DateTimeUtil.formatRelativeAdaptiveDateTime(
                context = context,
                from = now,
                to = breakTime
            )
        } else if (breakIn in -5..0) {
            context.getString(
                R.string.break_for,
                DateTimeUtil.formatMinutes(context, 5 - breakIn.absoluteValue)
            )
        } else {
            context.getString(
                R.string.break_was,
                DateTimeUtil.formatRelativeAdaptiveDateTime(
                    context = context,
                    from = now,
                    to = breakTime,
                    direction = RelativeDateTimeFormatter.Direction.LAST
                )
            )
        }
    }
}

@Composable
private fun rememberLessonTimeInterval(
    now: LocalDateTime,
    lessonStartTime: LocalDateTime,
    passed: Boolean
): String {
    val context = LocalContext.current

    return remember(now, lessonStartTime) {
        val status = if (passed) {
            context.getString(R.string.classes_ended_last)
        } else context.getString(R.string.class_started_last)
        "$status " + DateTimeUtil.formatRelativeAdaptiveDateTime(
            context = context,
            from = now,
            to = lessonStartTime,
            direction = RelativeDateTimeFormatter.Direction.LAST
        )
    }
}