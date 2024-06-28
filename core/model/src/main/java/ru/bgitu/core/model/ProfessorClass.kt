package ru.bgitu.core.model

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class ProfessorClass(
    val classroom: String,
    val building: String,
    val isLecture: Boolean,
    @SerialName("lessonDate")
    val date: LocalDate,
    val startAt: LocalTime,
    val endAt: LocalTime,
    @Transient
    val id: Long = 0L
)
