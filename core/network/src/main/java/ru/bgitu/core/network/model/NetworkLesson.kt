package ru.bgitu.core.network.model

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkLesson(
    @SerialName("id")
    val lessonId: Int,
    val subjectId: Int,
    val building: String,
    @SerialName("lessonDate")
    val date: LocalDate,
    @SerialName("startAt")
    val startAt: LocalTime,
    @SerialName("endAt")
    val endAt: LocalTime,
    val classroom: String,
    val teacher: String,
    val isLecture: Boolean
)
