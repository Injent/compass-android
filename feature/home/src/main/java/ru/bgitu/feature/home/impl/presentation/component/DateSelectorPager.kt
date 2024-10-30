package ru.bgitu.feature.home.impl.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus
import ru.bgitu.core.common.DateTimeUtil
import ru.bgitu.core.designsystem.theme.AppTheme

@Composable
fun DateSelectorPager(
    selectedDate: LocalDate,
    currentDate: LocalDate,
    pagerState: PagerState,
    onDateSelect: (LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {
    HorizontalPager(
        state = pagerState,
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = AppTheme.spacing.l),
        pageSpacing = AppTheme.spacing.l,
    ) { page ->
        val days = remember(currentDate, page) {
            DateTimeUtil.getWeekDates(
                fromDate = selectedDate.plus(
                    when (page) {
                        0 -> -2
                        1 -> -1
                        3 -> 1
                        4 -> 2
                        else -> 0
                    },
                    DateTimeUnit.WEEK
                )
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = modifier
                .fillMaxWidth(),
        ) {
            days.forEachIndexed { index, date ->
                DayOfWeekItem(
                    selected = date == selectedDate,
                    number = date.dayOfMonth.toString(),
                    weekdayName = remember(date) {
                        DateTimeUtil.formatWeek(
                            date = date,
                            short = true
                        )
                    },
                    onClick = { onDateSelect(date) },
                    compact = true,
                    isCurrentDay = date == currentDate,
                    modifier = Modifier.weight(1f)
                )
                if (days.lastIndex != index) {
                    Spacer(Modifier.width(DayItemTokens.Gap))
                }
            }
        }
    }
}