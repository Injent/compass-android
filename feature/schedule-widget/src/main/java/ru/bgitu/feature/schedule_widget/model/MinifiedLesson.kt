package ru.bgitu.feature.schedule_widget.model

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import ru.bgitu.core.model.Lesson
import ru.bgitu.feature.schedule_widget.WidgetPb.WidgetLessonPb

data class MinifiedLesson(
    val date: LocalDate,
    val startAt: LocalTime,
    val endAt: LocalTime,
    val isLecture: Boolean,
    val classroom: String,
    val building: String,
    val subject: String,
)

fun WidgetLessonPb.toMinifiedLesson(): MinifiedLesson {
    return MinifiedLesson(
        date = LocalDate.fromEpochDays(date),
        startAt = LocalTime.fromSecondOfDay(startAt),
        endAt = LocalTime.fromSecondOfDay(endAt),
        isLecture = isLecture, 
        classroom = classroom, 
        building = building, 
        subject = subject,
    )
}

fun Lesson.toWidgetLesson(): WidgetLessonPb = WidgetLessonPb.newBuilder()
    .setDate(date.toEpochDays())
    .setStartAt(startAt.toSecondOfDay())
    .setEndAt(endAt.toSecondOfDay())
    .setBuilding(building)
    .setClassroom(classroom)
    .setIsLecture(isLecture)
    .setSubject(subject.name)
    .build()