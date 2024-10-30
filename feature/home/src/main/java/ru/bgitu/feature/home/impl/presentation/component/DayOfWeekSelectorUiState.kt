package ru.bgitu.feature.home.impl.presentation.component

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.plus
import kotlinx.datetime.toJavaLocalDate
import ru.bgitu.core.common.DateTimeUtil

data class DayOfWeekSelectorUiState(
    val currentDateTime: LocalDateTime,
    val selectedDate: LocalDate
) {
    private val days = (-2..2).flatMap { weekOffset ->
        DateTimeUtil.getWeekDates(currentDateTime.date.plus(weekOffset, DateTimeUnit.WEEK))
    }.sortedBy { it.toEpochDays() }
    val pageCount = days.size
    val initialPage = days.indexOf(selectedDate)
    val dateBoundary = days.min().toJavaLocalDate()..days.max().toJavaLocalDate()

    fun getDateForPage(page: Int): LocalDate {
        val center = pageCount / 2
        return days[center - (center - page)]
    }

    fun getPageForDate(date: LocalDate): Int {
        return days.indexOf(date)
    }
}
