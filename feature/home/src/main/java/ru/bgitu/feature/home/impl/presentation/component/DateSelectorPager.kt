package ru.bgitu.feature.home.impl.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.Surface
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import kotlinx.datetime.LocalDate
import ru.bgitu.core.common.DateTimeUtil
import ru.bgitu.core.common.DateTimeUtil.currentDate
import ru.bgitu.core.designsystem.theme.AppTheme
import ru.bgitu.feature.home.impl.model.DayOfWeekSelectorUiState

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun DateSelectorPager(
    state: DayOfWeekSelectorUiState,
    pagerState: PagerState,
    onDateSelect: (LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {
    val adaptive = currentWindowAdaptiveInfo()
    val isCompact = adaptive.windowSizeClass.widthSizeClass <= WindowWidthSizeClass.Compact

    Box(modifier = modifier) {
        val alignModifier = when {
            pagerState.currentPage > state.dateInitialPage -> Modifier.align(Alignment.CenterStart)
            pagerState.currentPage < state.dateInitialPage -> Modifier.align(Alignment.CenterEnd)
            else -> Modifier.alpha(0f)
        }

        Surface(
            shape = AppTheme.shapes.small,
            color = AppTheme.colorScheme.foreground,
            modifier = alignModifier
                .zIndex(1f)
                .size(width = 6.dp, height = 40.dp)
        ) {}

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = AppTheme.spacing.l),
            pageSpacing = AppTheme.spacing.l,
        ) { page ->
            val days = remember(page) {
                DateTimeUtil.getWeekDates(state.getDateFromDatePager(page))
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth(),
            ) {
                days.forEachIndexed { index, date ->
                    DayOfWeekItem(
                        selected = date == state.selectedDate,
                        number = date.dayOfMonth.toString(),
                        weekdayName = remember(date) {
                            DateTimeUtil.formatWeek(
                                date = date,
                                short = isCompact
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
}