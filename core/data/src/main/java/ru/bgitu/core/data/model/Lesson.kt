package ru.bgitu.core.data.model

import ru.bgitu.core.datastore.model.StoredLesson
import ru.bgitu.core.datastore.model.StoredSchedule
import ru.bgitu.core.model.Lesson
import ru.bgitu.core.model.Subject
import ru.bgitu.core.network.model.NetworkLesson
import ru.bgitu.core.network.model.response.LessonResponse
import ru.bgitu.core.network.model.response.ScheduleResponse

fun LessonResponse.toStoredLesson(): StoredLesson = StoredLesson(
    subjectId = subjectId,
    subjectName = subjectName,
    building = building,
    startAt = startAt,
    endAt = endAt,
    classroom = classroom,
    teacher = teacher,
    isLecture = isLecture
)

fun ScheduleResponse.toStoredSchedule(): StoredSchedule {
    return StoredSchedule(
        firstWeek = firstWeek.mapValues {
            (_, lessons) -> lessons.map(LessonResponse::toStoredLesson)
        },
        secondWeek = secondWeek.mapValues {
            (_, lessons) -> lessons.map(LessonResponse::toStoredLesson)
        },
    )
}