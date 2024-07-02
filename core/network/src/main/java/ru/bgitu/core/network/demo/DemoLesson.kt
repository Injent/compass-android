package ru.bgitu.core.network.demo

import kotlinx.datetime.LocalTime
import ru.bgitu.core.common.DateTimeUtil
import ru.bgitu.core.network.model.NetworkLesson

internal val demoLessons by lazy {
    val currentDate = DateTimeUtil.currentDate

    listOf(
        NetworkLesson(
            lessonId = 1,
            subjectId = 1,
            building = "1",
            date = currentDate,
            startAt = LocalTime(hour = 8, minute = 20),
            endAt = LocalTime(hour = 9, minute = 5),
            classroom = "308",
            teacher = demoTeachers[0],
            isLecture = true
        ),
        NetworkLesson(
            lessonId = 2,
            subjectId = 2,
            building = "1",
            date = currentDate,
            startAt = LocalTime(hour = 9, minute = 10),
            endAt = LocalTime(hour = 10, minute = 5),
            classroom = "311",
            teacher = demoTeachers[1],
            isLecture = false
        ),
        NetworkLesson(
            lessonId = 3,
            subjectId = 3,
            building = "1",
            date = currentDate,
            startAt = LocalTime(hour = 10, minute = 10),
            endAt = LocalTime(hour = 11, minute = 50),
            classroom = "228",
            teacher = demoTeachers[2],
            isLecture = false
        ),
        NetworkLesson(
            lessonId = 4,
            subjectId = 4,
            building = "1",
            date = currentDate,
            startAt = LocalTime(hour = 12, minute = 15),
            endAt = LocalTime(hour = 13, minute = 25),
            classroom = "308",
            teacher = demoTeachers[3],
            isLecture = false
        )
    )
}