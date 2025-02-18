package ru.bgitu.core.datastore.model

import kotlinx.datetime.LocalTime
import ru.bgitu.core.LessonPb

data class StoredLesson(
    val subjectId: Int,
    val subjectName: String,
    val building: String,
    val startAt: LocalTime,
    val endAt: LocalTime,
    val classroom: String,
    val teacher: String,
    val isLecture: Boolean
)

fun LessonPb.toStoredLesson(): StoredLesson = StoredLesson(
    subjectId = subjectId,
    subjectName = subjectName,
    building = building,
    startAt = LocalTime.fromSecondOfDay(startAt),
    endAt = LocalTime.fromSecondOfDay(endAt),
    classroom = classroom,
    teacher = teacher,
    isLecture = isLecture
)