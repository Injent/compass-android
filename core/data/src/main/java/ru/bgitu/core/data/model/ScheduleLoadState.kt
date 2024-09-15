package ru.bgitu.core.data.model

import kotlinx.datetime.LocalDate
import ru.bgitu.core.model.Lesson

sealed class ScheduleLoadState {
    data class Success(
        private val data: Map<LocalDate, List<Lesson>>
    ) : ScheduleLoadState() {
        operator fun get(date: LocalDate): List<Lesson> {
            return data[date] ?: emptyList()
        }
    }
    data object Loading : ScheduleLoadState()
    data object Conflict : ScheduleLoadState()
    data class Error(val throwable: Throwable?) : ScheduleLoadState()
}