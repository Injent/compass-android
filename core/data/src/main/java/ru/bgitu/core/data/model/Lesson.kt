package ru.bgitu.core.data.model

import ru.bgitu.core.database.entity.LessonEntity
import ru.bgitu.core.model.Lesson
import ru.bgitu.core.model.Subject
import ru.bgitu.core.network.model.NetworkLesson

fun NetworkLesson.toEntity() = LessonEntity(
    id = lessonId,
    building = building,
    subjectId = subjectId,
    subjectName = subjectName,
    date = date,
    startAt = startAt,
    endAt = endAt,
    classroom = classroom,
    teacher = teacher,
    isLecture = isLecture
)

fun LessonEntity.toExternalModel() = Lesson(
    lessonId = id,
    building = building,
    date = date,
    subject = Subject(
        subjectId = subjectId,
        name = subjectName
    ),
    startAt = startAt,
    endAt = endAt,
    classroom = classroom,
    teacher = teacher,
    isLecture = isLecture
)

fun NetworkLesson.toExternalModel() = Lesson(
    lessonId = lessonId,
    building = building,
    date = date,
    subject = Subject(
        subjectId = subjectId,
        name = subjectName
    ),
    startAt = startAt,
    endAt = endAt,
    classroom = classroom,
    teacher = teacher,
    isLecture = isLecture
)