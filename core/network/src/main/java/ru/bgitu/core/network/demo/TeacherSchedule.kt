package ru.bgitu.core.network.demo

import kotlinx.datetime.LocalTime
import ru.bgitu.core.common.DateTimeUtil
import ru.bgitu.core.common.DateTimeUtil.currentDate
import ru.bgitu.core.model.ProfessorClass

internal val teachersSchedule = mapOf(
    demoTeachers[0] to listOf(
        ProfessorClass(
            classroom = "308",
            building = "1",
            isLecture = true,
            date = DateTimeUtil.currentDate,
            startAt = LocalTime(hour = 8, minute = 20),
            endAt = LocalTime(hour = 9, minute = 5),
        )
    ),
    demoTeachers[1] to listOf(
        ProfessorClass(
            building = "1",
            date = currentDate,
            startAt = LocalTime(hour = 9, minute = 10),
            endAt = LocalTime(hour = 10, minute = 5),
            classroom = "311",
            isLecture = false
        )
    ),
    demoTeachers[2] to listOf(
        ProfessorClass(
            building = "1",
            date = currentDate,
            startAt = LocalTime(hour = 10, minute = 10),
            endAt = LocalTime(hour = 11, minute = 50),
            classroom = "228",
            isLecture = false
        ),
    ),
    demoTeachers[3] to listOf(
        ProfessorClass(
            building = "1",
            date = currentDate,
            startAt = LocalTime(hour = 12, minute = 15),
            endAt = LocalTime(hour = 13, minute = 25),
            classroom = "308",
            isLecture = false
        )
    )
)