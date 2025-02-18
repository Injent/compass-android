package ru.bgitu.core.data.model

import kotlinx.datetime.LocalDate
import ru.bgitu.core.common.DateTimeUtil
import ru.bgitu.core.common.TextResource
import ru.bgitu.core.datastore.model.StoredLesson
import ru.bgitu.core.datastore.model.StoredSchedule

sealed interface ScheduleState {
    data class Loaded(
        internal val schedule: StoredSchedule
    ) : ScheduleState
    data object Loading : ScheduleState
    data class Error(val details: TextResource) : ScheduleState
}

fun ScheduleState.Loaded.getLessons(date: LocalDate): List<StoredLesson> {
    return if (DateTimeUtil.isOddWeek(date)) {
        schedule.firstWeek.getOrDefault(date.dayOfWeek, emptyList())
    } else {
        schedule.secondWeek.getOrDefault(date.dayOfWeek, emptyList())
    }
}