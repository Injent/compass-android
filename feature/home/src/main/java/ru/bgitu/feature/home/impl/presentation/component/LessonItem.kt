package ru.bgitu.feature.home.impl.presentation.component

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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.atDate
import kotlinx.datetime.atTime
import ru.bgitu.core.common.CommonStrings
import ru.bgitu.core.common.DateTimeUtil
import ru.bgitu.core.common.LessonDataUtils
import ru.bgitu.core.datastore.model.StoredLesson
import ru.bgitu.core.designsystem.components.AppCard
import ru.bgitu.core.designsystem.icon.AppIcons
import ru.bgitu.core.designsystem.icon.Building
import ru.bgitu.core.designsystem.icon.Flask
import ru.bgitu.core.designsystem.icon.OpenBook
import ru.bgitu.core.designsystem.icon.PcOutline
import ru.bgitu.core.designsystem.icon.PersonCard
import ru.bgitu.core.designsystem.theme.AppRippleTheme
import ru.bgitu.core.designsystem.theme.AppTheme
import ru.bgitu.core.designsystem.util.SoftwareLayer
import ru.bgitu.core.ui.onClick
import ru.bgitu.feature.home.R
import java.time.temporal.ChronoUnit
import kotlin.math.absoluteValue

data class LessonVisualData(
    val isFirst: Boolean,
    val isLast: Boolean,
    val isPassed: Boolean,
    val isHighlighted: Boolean,
    val minTimeWidth: Dp
)

@Composable
fun LessonItem(
    lesson: StoredLesson,
    lessonDate: LocalDate,
    now: LocalDateTime,
    onClick: () -> Unit,
    onAddNote: () -> Unit,
    data: LessonVisualData,
    modifier: Modifier = Modifier,
    expanded: Boolean = false,
) {
    val layoutParams = remember {
        LessonLayoutParams(
            indicatorSpaceWidth = 12.dp,
            gap = 24.dp,
            verticalGap = 8.dp,
            firstItem = data.isFirst
        )
    }
    LessonLayout(
        modifier = modifier,
        params = layoutParams
    ) {
        val startTime = remember(lesson.startAt) {
            DateTimeUtil.formatTime(lesson.startAt)
        }
        val endTime = remember(lesson.endAt) {
            DateTimeUtil.formatTime(lesson.endAt)
        }
        val breakInterval = rememberBreakTimeInterval(
            now = now,
            breakTime = lessonDate.atTime(
                LocalTime.fromSecondOfDay(lesson.startAt.toSecondOfDay() + (45 * 60))
            )
        )
        val classInterval = rememberLessonTimeInterval(
            now = now,
            lessonStartTime = if (data.isPassed) {
                lesson.endAt.atDate(lessonDate)
            } else {
                lesson.startAt.atDate(lessonDate)
            },
            passed = data.isPassed
        )

        Text(
            text = buildAnnotatedString {
                append(startTime)
                withStyle(
                    AppTheme.typography.callout.toSpanStyle()
                        .copy(color = AppTheme.colorScheme.foreground3)
                ) {
                    appendLine()
                    append(endTime)
                }
            },
            style = AppTheme.typography.headline2,
            color = AppTheme.colorScheme.foreground1,
            maxLines = 2,
            overflow = TextOverflow.Visible,
            modifier = Modifier
                .width(data.minTimeWidth)
                .layoutId(LessonComponentId.TIME)
        )
        LessonIndicator(
            options = LessonIndicatorOptions(
                defaultStrokeWidth = 2.4.dp,
                dashInterval = 4.dp,
                dashWidth = 3.dp,
                passed = data.isPassed,
                highlighted = data.isHighlighted,
                isFirst = data.isFirst,
            ),
            modifier = Modifier.layoutId(LessonComponentId.INDICATOR)
        )
        LessonBreakPoint(
            smallPoint = false,
            enabled = data.isPassed || data.isHighlighted,
            animated = data.isHighlighted,
            highlighted = data.isPassed || data.isHighlighted,
            modifier = Modifier.layoutId(LessonComponentId.LONG_BREAK),
            label = classInterval
        )
        if (lessonDate == now.date && !data.isPassed) {
            LessonBreakPoint(
                smallPoint = true,
                highlighted = data.isHighlighted,
                label = breakInterval,
                modifier = Modifier.layoutId(LessonComponentId.SHORT_BREAK)
            )
        }

        AppRippleTheme {
            AnimatedContent(
                targetState = expanded,
                label = "expand_state",
                transitionSpec = {
                    expandVertically { it } togetherWith shrinkVertically { it }
                },
                modifier = Modifier
                    .layoutId(LessonComponentId.LESSON_CARD)
                    .clip(AppTheme.shapes.default)
                    .clickable { onClick() }
            ) { isExpanded ->
                LessonCard(
                    lesson = lesson,
                    expanded = isExpanded,
                    onAddNote = onAddNote,
                    modifier = Modifier
                )
            }
        }

        if (data.isLast) {
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
    lesson: StoredLesson,
    expanded: Boolean,
    onAddNote: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    AppCard(
        contentPadding = PaddingValues(
            start = AppTheme.spacing.m,
            end = AppTheme.spacing.m,
            top = AppTheme.spacing.m,
            bottom = AppTheme.spacing.m
        ),
        color = AppTheme.colorScheme.backgroundTouchable,
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = buildAnnotatedString {
                    append(lesson.subjectName)

                    lesson.teacher.takeIf { it.isNotEmpty() }?.let { teacherName ->
                        appendLine()
                        withStyle(
                            AppTheme.typography.callout.toSpanStyle()
                                .copy(
                                    fontSize = 13.5.sp,
                                    color = AppTheme.colorScheme.foreground2
                                )
                        ) {
                            appendInlineContent(PERSON_CARD_ID)
                            append(" ")
                            append(teacherName)
                        }
                    }

                    withStyle(
                        AppTheme.typography.callout.toSpanStyle()
                            .copy(color = AppTheme.colorScheme.foreground2)
                    ) {
                        appendLine()
                        if (lesson.building.equals("ДОТ", true)) {
                            appendInlineContent(PC_ID)
                        } else {
                            appendInlineContent(BUILDING_ID)
                        }
                        append(" ")
                        remember(lesson) {
                            LessonDataUtils.provideLocation(
                                context = context,
                                building = lesson.building,
                                classroom = lesson.classroom,
                                style = LessonDataUtils.DisplayStyle.DEFAULT
                            )
                        }.also(::append)
                    }
                },
                style = AppTheme.typography.body,
                color = AppTheme.colorScheme.foreground1,
                modifier = Modifier.weight(1f),
                overflow = TextOverflow.Ellipsis,
                inlineContent = lessonInlineContent
            )
            Text(
                text = buildAnnotatedString {
                    append(
                        stringResource(
                            if (lesson.isLecture) {
                                CommonStrings.lecture_abbriviation
                            } else CommonStrings.practice_abbriviation
                        ) + " "
                    )

                    appendInlineContent(if (lesson.isLecture) OPEN_BOOK_ID else FLASK_ID)
                },
                style = AppTheme.typography.callout,
                color = AppTheme.colorScheme.foreground2,
                lineHeight = AppTheme.typography.callout.fontSize,
                modifier = Modifier.padding(end = 2.dp),
                fontWeight = FontWeight.Medium,
                inlineContent = lessonInlineContent,
            )
        }

//        AnimatedVisibility(
//            visible = expanded,
//            enter = expandVertically(),
//            exit = shrinkVertically()
//        ) {
//            DashedOutlinedButton(
//                text = stringResource(R.string.action_add_homework),
//                onClick = onAddNote,
//                modifier = Modifier
//                    .padding(top = AppTheme.spacing.s)
//                    .fillMaxWidth()
//            )
//        }
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
            return@remember context.getString(R.string.break_at) + " " + DateTimeUtil.formatTime(
                breakTime.time
            )
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

private const val BUILDING_ID = "building"
private const val PERSON_CARD_ID = "person_card"
private const val FLASK_ID = "flask"
private const val OPEN_BOOK_ID = "book"
private const val PC_ID = "devices"

private val lessonInlineContent = mapOf(
    BUILDING_ID to InlineTextContent(
        placeholder = Placeholder(
            width = 1.em,
            height = 1.em,
            placeholderVerticalAlign = PlaceholderVerticalAlign.TextTop
        )
    ) {
        Icon(
            imageVector = AppIcons.Building,
            contentDescription = null,
            tint = AppTheme.colorScheme.foreground2,
            modifier = Modifier
        )
    },
    PERSON_CARD_ID to InlineTextContent(
        placeholder = Placeholder(
            width = 1.em,
            height = 1.em,
            placeholderVerticalAlign = PlaceholderVerticalAlign.TextTop
        )
    ) {
        Icon(
            imageVector = AppIcons.PersonCard,
            contentDescription = null,
            tint = AppTheme.colorScheme.foreground2,
            modifier = Modifier
        )
    },
    FLASK_ID to InlineTextContent(
        placeholder = Placeholder(
            width = 10.sp,
            height = 10.sp,
            placeholderVerticalAlign = PlaceholderVerticalAlign.TextTop
        )
    ) {
        Icon(
            imageVector = AppIcons.Flask,
            contentDescription = null,
            tint = AppTheme.colorScheme.foreground2,
            modifier = Modifier
        )
    },
    OPEN_BOOK_ID to InlineTextContent(
        placeholder = Placeholder(
            width = 11.sp,
            height = 11.sp,
            placeholderVerticalAlign = PlaceholderVerticalAlign.TextTop
        )
    ) {
        Icon(
            imageVector = AppIcons.OpenBook,
            contentDescription = null,
            tint = AppTheme.colorScheme.foreground2,
            modifier = Modifier
        )
    },
    PC_ID to InlineTextContent(
        placeholder = Placeholder(
            width = 1.em,
            height = 1.em,
            placeholderVerticalAlign = PlaceholderVerticalAlign.TextTop
        )
    ) {
        Icon(
            imageVector = AppIcons.PcOutline,
            contentDescription = null,
            tint = AppTheme.colorScheme.foreground2,
            modifier = Modifier
        )
    }
)