package ru.bgitu.core.database.model

import ru.bgitu.core.model.Lesson

data class DaySchedule(
    val lessons: List<Lesson>,
    val isWeekend: Boolean
)