package ru.bgitu.feature.home.impl.model

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.daysUntil
import kotlinx.datetime.plus
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toKotlinLocalDate
import java.time.temporal.TemporalAdjusters
import kotlin.math.abs

private const val STUDY_DAYS_IN_WEEK = 6


class DayOfWeekSelectorUiState(
    val now: LocalDateTime,
    val selectedDate: LocalDate,
    initialDate: LocalDate? = null,
) {
    val pageCount = 1000
    private val centerPage = pageCount / 2
    val initialPage = initialDate?.let { getPage(it) } ?: centerPage
    val datePageCount = pageCount / STUDY_DAYS_IN_WEEK
    val dateInitialPage = initialPage / STUDY_DAYS_IN_WEEK

    fun getDateFromDatePager(datePage: Int): LocalDate {
        val weekOffset = datePage - (datePageCount / 2)
        return now.date.plus(weekOffset, DateTimeUnit.WEEK)
    }

    fun getPageFromDatePager(date: LocalDate, page: Int): Int {
        val sta = date.toJavaLocalDate()
            .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
            .toKotlinLocalDate()
        val diff = sta
            .daysUntil(date)

        return ((page - diff) / STUDY_DAYS_IN_WEEK).let { page ->
            if (now.dayOfWeek > DayOfWeek.WEDNESDAY) {
                page + 1
            } else page
        }
    }

    fun getDate(page: Int): LocalDate {
        val startDate = now.date.toJavaLocalDate()
        val daysOffset = page - centerPage
        var currentDate = startDate
        var actualDaysCount = 0
        val direction = if (daysOffset > 0) 1 else -1

        while (actualDaysCount < abs(daysOffset)) {
            currentDate = currentDate.plusDays(direction.toLong())

            if (currentDate.dayOfWeek.value != 7) {
                actualDaysCount++
            }
        }

        return currentDate.toKotlinLocalDate()
    }

    fun getPage(date: LocalDate): Int {
        val targetDate = date.toJavaLocalDate()
        val startDate = now.date.toJavaLocalDate()
        var currentDate = startDate
        var currentPage = centerPage
        val direction = if (targetDate.isAfter(startDate)) 1 else -1

        while (currentDate != targetDate) {
            currentDate = currentDate.plusDays(direction.toLong())

            if (currentDate.dayOfWeek.value != 7) {
                currentPage += direction
            }

            if (currentDate == targetDate) break
        }

        return currentPage
    }
}
