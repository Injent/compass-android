package ru.bgitu.core.ui.schedule

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import kotlinx.datetime.LocalDate
import ru.bgitu.core.common.DateTimeUtil
import ru.bgitu.core.designsystem.components.AppCard
import ru.bgitu.core.designsystem.theme.AppTheme

private const val SIX_DATES = 6

object DayItemTokens {
    val Width = 54.dp
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
    val density = LocalDensity.current
    var shouldUseWeight by remember { mutableStateOf(false) }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
            .onPlaced { coordinates ->
                with(density) {
                    val itemsWidth = SIX_DATES * DayItemTokens.Width
                    val gapWidth = (SIX_DATES - 1) * DayItemTokens.Gap
                    shouldUseWeight = (itemsWidth + gapWidth).toPx() > coordinates.size.width ||
                            windowSize.widthSizeClass > WindowWidthSizeClass.Compact
                }
            },
    ) {
        val days = remember(state.selectedDate) {
            DateTimeUtil.getWeekDates(state.selectedDate)
        }
        days.forEach { date ->
            val week = remember(date, windowSize) {
                DateTimeUtil.formatWeek(
                    date = date,
                    short = windowSize.widthSizeClass <= WindowWidthSizeClass.Compact
                )
            }

            DayOfWeekItem(
                selected = date == state.selectedDate,
                number = date.dayOfMonth.toString(),
                weekdayName = week,
                onClick = { onDateSelected(date) },
                compact = windowSize.widthSizeClass <= WindowWidthSizeClass.Compact,
                modifier = Modifier.then(
                    if (shouldUseWeight) {
                        Modifier.weight(1f)
                    } else Modifier.width(DayItemTokens.Width)
                )
            )
            if (days.last() != date) {
                Spacer(Modifier.width(DayItemTokens.Gap))
            }
        }
    }
}

@Composable
private fun DayOfWeekItem(
    selected: Boolean,
    number: String,
    weekdayName: String,
    onClick: () -> Unit,
    compact: Boolean,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (selected) {
            AppTheme.colorScheme.foreground
        } else AppTheme.colorScheme.backgroundTouchable,
        label = "BackgroundTransient"
    )

    val dateTextColor by animateColorAsState(
        targetValue = if (selected) {
            AppTheme.colorScheme.foregroundOnBrand
        } else if (enabled) {
            AppTheme.colorScheme.foreground1
        } else AppTheme.colorScheme.foregroundDisabled,
        label = ""
    )

    val dayOfWeekTextColor by animateColorAsState(
        targetValue = if (selected) {
            AppTheme.colorScheme.foregroundOnBrand
        } else if (enabled) {
            AppTheme.colorScheme.foreground3
        } else AppTheme.colorScheme.foregroundDisabled,
        label = ""
    )

    AppCard(
        contentPadding = if (compact) {
            PaddingValues()
        } else {
            PaddingValues(horizontal = AppTheme.spacing.s)
        },
        color = backgroundColor,
        shadowEnabled = false,
        onClick = if (enabled) onClick else null,
        modifier = modifier.height(DayItemTokens.Height)
    ) {
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
                horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.s),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxSize()
            ) {
                DayItemContent(
                    number = number,
                    weekdayName = weekdayName,
                    dateTextColor = dateTextColor,
                    dayOfWeekTextColor = dayOfWeekTextColor
                )
            }
        }
    }
}

@Composable
private fun DayItemContent(
    number: String,
    weekdayName: String,
    dateTextColor: Color,
    dayOfWeekTextColor: Color
) {
    Text(
        text = number,
        style = AppTheme.typography.body,
        color = dateTextColor,
        maxLines = 1
    )
    Text(
        text = weekdayName,
        style = AppTheme.typography.callout,
        color = dayOfWeekTextColor,
        overflow = TextOverflow.Ellipsis,
        maxLines = 1
    )
}