package ru.bgitu.feature.professor_search.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.datetime.LocalDate
import ru.bgitu.core.common.DateTimeUtil
import ru.bgitu.core.common.LessonDataUtils
import ru.bgitu.core.designsystem.icon.AppIcons
import ru.bgitu.core.designsystem.theme.AppTheme
import ru.bgitu.core.designsystem.util.MeasureComposable
import ru.bgitu.core.model.ProfessorClass
import ru.bgitu.feature.professor_search.R

internal fun LazyListScope.teacherEmptyDay(
    date: LocalDate,
    maxWidthOfDayAndWeek: Dp,
    modifier: Modifier = Modifier
) {
    item {
        Row(
            horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.l),
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier.fillMaxWidth(),
        ) {
            DateWithWeek(date = date, modifier = Modifier.width(maxWidthOfDayAndWeek))
            Text(
                text = stringResource(R.string.not_holding_classes),
                style = AppTheme.typography.headline1,
                fontWeight = FontWeight.Normal,
                color = AppTheme.colorScheme.foreground3,
                modifier = Modifier.padding(start = AppTheme.spacing.l)
            )
        }
    }
}

internal fun LazyListScope.teacherClassesGroup(
    classes: List<ProfessorClass>,
    date: LocalDate,
    maxWidthOfDayAndWeek: Dp,
    timeColumnWidth: Dp,
    modifier: Modifier = Modifier
) {
    item {
        Row(
            horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.l),
            modifier = modifier.animateItem(),
        ) {
            DateWithWeek(date = date, modifier = Modifier.width(maxWidthOfDayAndWeek))
            Column(
                modifier = Modifier.weight(1f),
            ) {
                classes.forEach { lesson ->
                    TeacherClassItem(
                        lesson = lesson,
                        isFirst = classes.first() == lesson,
                        isLast = classes.last() == lesson,
                        timeColumnWidth = timeColumnWidth,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
internal fun TeacherClassItem(
    lesson: ProfessorClass,
    isFirst: Boolean,
    isLast: Boolean,
    timeColumnWidth: Dp,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val classLocation = remember(lesson) {
        LessonDataUtils.provideLocation(
            context = context,
            building = lesson.building,
            classroom = lesson.classroom,
            style = LessonDataUtils.DisplayStyle.DEFAULT
        )
    }

    Surface(
        shape = when {
            isFirst && isLast -> AppTheme.shapes.default
            isFirst -> RoundedRectangleFirst
            isLast -> RoundedRectangleLast
            else -> AppTheme.shapes.extraSmall
        },
        color = AppTheme.colorScheme.backgroundTouchable,
        modifier = modifier,
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.m),
            modifier = Modifier
                .padding(
                    horizontal = AppTheme.spacing.l,
                    vertical = AppTheme.spacing.m
                )
        ) {
            Text(
                text = remember {
                    DateTimeUtil.formatTime(lesson.startAt) + "\n" + DateTimeUtil.formatTime(lesson.endAt)
                },
                style = AppTheme.typography.callout,
                color = AppTheme.colorScheme.foreground1,
                modifier = Modifier.width(timeColumnWidth)
            )

            Column(Modifier.fillMaxHeight().weight(1f)) {
                Text(
                    text = classLocation,
                    style = AppTheme.typography.headline2,
                    color = AppTheme.colorScheme.foreground1,
                    modifier = Modifier
                )
                Text(
                    text = "Математика",
                    style = AppTheme.typography.headline2,
                    color = AppTheme.colorScheme.foreground2,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier
                )
            }

            Icon(
                painter = painterResource(if (lesson.isLecture) AppIcons.BookCover2 else AppIcons.Flask2),
                contentDescription = null,
                tint = AppTheme.colorScheme.foreground2,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .size(16.dp)
            )
        }
    }
    if (!isLast)
        Spacer(Modifier.height(3.dp))
}

@Composable
internal fun maxWidthOfDayAndWeek(): State<Dp> {
    val maxWidthOfDayAndWeekView = remember { mutableStateOf(0.dp) }

    MeasureComposable(
        composable = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "000",
                    style = AppTheme.typography.headline2,
                    color = AppTheme.colorScheme.foreground3
                )
                Text(
                    text = "00",
                    color = AppTheme.colorScheme.foreground1,
                    style = AppTheme.typography.title3
                )
            }
        }
    ) { size ->
        maxWidthOfDayAndWeekView.value = size.width
    }

    return maxWidthOfDayAndWeekView
}

@Composable
private fun DateWithWeek(
    date: LocalDate,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {
        Text(
            text = remember(date) { DateTimeUtil.formatWeek(date).uppercase() },
            style = AppTheme.typography.headline2,
            color = AppTheme.colorScheme.foreground3
        )
        Text(
            text = "${date.dayOfMonth}",
            color = AppTheme.colorScheme.foreground1,
            style = AppTheme.typography.title3
        )
    }
}

private val RoundedRectangleFirst = RoundedCornerShape(
    topStart = 12.dp,
    topEnd = 12.dp,
    bottomStart = 4.dp,
    bottomEnd = 4.dp
)

private val RoundedRectangleLast = RoundedCornerShape(
    topStart = 4.dp,
    topEnd = 4.dp,
    bottomStart = 12.dp,
    bottomEnd = 12.dp
)