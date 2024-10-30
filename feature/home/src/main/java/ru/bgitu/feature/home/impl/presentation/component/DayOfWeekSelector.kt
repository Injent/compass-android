package ru.bgitu.feature.home.impl.presentation.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.datetime.LocalDate
import ru.bgitu.core.common.DateTimeUtil
import ru.bgitu.core.designsystem.components.AppCard
import ru.bgitu.core.designsystem.theme.AppTheme

object DayItemTokens {
    val Height = 56.dp
    val Gap = 8.dp
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun DayOfWeekSelector(
    state: DayOfWeekSelectorUiState,
    onDateSelected: (LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {
    val windowSize = currentWindowAdaptiveInfo().windowSizeClass
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth(),
    ) {
        val days = remember(state.selectedDate) {
            DateTimeUtil.getWeekDates(state.selectedDate)
        }
        days.forEach { date ->
            val week = remember(date, windowSize) {
                DateTimeUtil.formatWeek(
                    date = date,
                    short = windowSize.widthSizeClass <= WindowWidthSizeClass.Expanded
                )
            }

            DayOfWeekItem(
                selected = date == state.selectedDate,
                number = date.dayOfMonth.toString(),
                weekdayName = week,
                onClick = { onDateSelected(date) },
                compact = windowSize.widthSizeClass <= WindowWidthSizeClass.Compact,
                isCurrentDay = date == state.currentDateTime.date,
                modifier = Modifier.weight(1f)
            )
            if (days.last() != date) {
                Spacer(Modifier.width(DayItemTokens.Gap))
            }
        }
    }
}

@Composable
fun DayOfWeekItem(
    selected: Boolean,
    number: String,
    weekdayName: String,
    onClick: () -> Unit,
    compact: Boolean,
    isCurrentDay: Boolean,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val animationSpec = tween<Color>(400)
    val backgroundColor by animateColorAsState(
        targetValue = if (selected) {
            AppTheme.colorScheme.foreground
        } else AppTheme.colorScheme.backgroundTouchable,
        animationSpec = animationSpec,
        label = "BackgroundTransient"
    )

    val dateTextColor by animateColorAsState(
        targetValue = if (selected) {
            AppTheme.colorScheme.foregroundOnBrand
        } else if (enabled) {
            AppTheme.colorScheme.foreground1
        } else AppTheme.colorScheme.foregroundDisabled,
        animationSpec = animationSpec,
        label = ""
    )

    val dayOfWeekTextColor by animateColorAsState(
        targetValue = if (selected) {
            AppTheme.colorScheme.foregroundOnBrand
        } else if (enabled) {
            AppTheme.colorScheme.foreground3
        } else AppTheme.colorScheme.foregroundDisabled,
        animationSpec = animationSpec,
        label = ""
    )

    AppCard(
        contentPadding = PaddingValues(),
        color = if (isCurrentDay) AppTheme.colorScheme.backgroundTouchable else backgroundColor,
        onClick = if (enabled) onClick else null,
        modifier = modifier.height(DayItemTokens.Height)
    ) {
        Box(
            contentAlignment = Alignment.Center,
        ) {
            if (isCurrentDay) {
                val animatedCornerSize by animateDpAsState(
                    targetValue = if (selected) 12.dp else 50.dp
                )
                val animatedWidth by animateFloatAsState(
                    targetValue = if (selected) 1f else 0.4f
                )
                val animatedHeight by animateFloatAsState(
                    targetValue = if (selected) 1f else 0.07f
                )

                Spacer(
                    Modifier
                        .fillMaxHeight(animatedHeight)
                        .fillMaxWidth(animatedWidth)
                        .background(AppTheme.colorScheme.foreground, RoundedCornerShape(animatedCornerSize))
                        .align(Alignment.BottomCenter)
                )
            }
            if (compact) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize()
                ) {
                    DayItemContent(
                        number = number,
                        weekdayName = weekdayName,
                        dateTextColor = dateTextColor,
                        dayOfWeekTextColor = dayOfWeekTextColor
                    )
                }
            } else {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(AppTheme.spacing.s)
                ) {
                    DayItemContent(
                        number = number,
                        weekdayName = weekdayName,
                        dateTextColor = dateTextColor,
                        dayOfWeekTextColor = dayOfWeekTextColor,
                        space = AppTheme.spacing.s
                    )
                }
            }
        }
    }
}

@Composable
private fun DayItemContent(
    number: String,
    weekdayName: String,
    dateTextColor: Color,
    dayOfWeekTextColor: Color,
    space: Dp = 0.dp,
) {
    Text(
        text = number,
        style = AppTheme.typography.body,
        color = dateTextColor,
        maxLines = 1,
        modifier = Modifier
            .padding(end = space)
    )
    Text(
        text = weekdayName,
        style = AppTheme.typography.callout,
        color = dayOfWeekTextColor,
        overflow = TextOverflow.Ellipsis,
        maxLines = 1
    )
}