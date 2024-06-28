package ru.bgitu.core.notifications.model

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class ScheduleChangeData(
    val text: String,
    val date: LocalDate,
    val lessonIds: List<Int>
)