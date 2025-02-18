package ru.bgitu.core.network.model.response

import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ScheduleResponse(
    @SerialName("first_week") val firstWeek: Map<DayOfWeek, List<LessonResponse>>,
    @SerialName("second_week") val secondWeek: Map<DayOfWeek, List<LessonResponse>>
)

@Serializable
data class LessonResponse(
    @SerialName("subjectId") val subjectId: Int,
    @SerialName("subjectName") val subjectName: String,
    @SerialName("building") val building: String,
    @SerialName("startAt") val startAt: LocalTime,
    @SerialName("endAt") val endAt: LocalTime,
    @SerialName("classroom") val classroom: String,
    @SerialName("teacher") val teacher: String,
    @SerialName("isLecture") val isLecture: Boolean
)