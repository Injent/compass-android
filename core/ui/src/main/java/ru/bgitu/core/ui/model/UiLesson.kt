package ru.bgitu.core.ui.model

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.atDate
import kotlinx.datetime.atTime
import ru.bgitu.core.common.DateTimeUtil
import ru.bgitu.core.model.Lesson

data class UiLesson(
    val name: String,
    val teacher: String,
    val building: String,
    val classroom: String,
    val isLecture: Boolean,
    val startTime: LocalTime,
    val endTime: LocalTime,
    val date: LocalDate,
    val passed: Boolean,
    val highlighted: Boolean,
    val isFirst: Boolean,
    val isLast: Boolean,
    val breakTime: LocalDateTime
)

fun Lesson.toUiModel(isFirst: Boolean, isLast: Boolean) = UiLesson(
    name = subject.name,
    teacher = teacher,
    building = building,
    classroom = classroom,
    isLecture = isLecture,
    startTime = startAt,
    endTime = endAt,
    date = date,
    passed = DateTimeUtil.currentDateTime.let {
        date.atTime(endAt) <= it
    },
    highlighted = run {
        val now = DateTimeUtil.currentDateTime
        val start = startAt.atDate(date)
        val end = endAt.atDate(date)
        now in start..end
    },
    isFirst = isFirst,
    isLast = isLast,
    breakTime = startDateTime.date.atTime(
        LocalTime.fromSecondOfDay(startAt.toSecondOfDay() + (45 * 60))
    )
)