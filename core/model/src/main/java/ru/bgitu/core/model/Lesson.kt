package ru.bgitu.core.model

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.atTime
import kotlinx.serialization.Serializable

@Serializable
data class Lesson(
    val lessonId: Int,
    val building: String,
    val date: LocalDate,
    val subject: Subject,
    val startAt: LocalTime,
    val endAt: LocalTime,
    val classroom: String,
    val teacher: String,
    val isLecture: Boolean
) {
    val startDateTime: LocalDateTime
        get() = date.atTime(startAt)
    val endDateTime: LocalDateTime
        get() = date.atTime(endAt)
}