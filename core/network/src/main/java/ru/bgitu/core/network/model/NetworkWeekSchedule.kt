package ru.bgitu.core.network.model

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class NetworkWeekSchedule(
    val weekId: Int,
    val startAt: LocalDate,
    val endAt: LocalDate,
    val lessons: List<NetworkLesson>,
)
