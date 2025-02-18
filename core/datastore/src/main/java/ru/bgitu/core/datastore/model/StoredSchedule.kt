package ru.bgitu.core.datastore.model

import kotlinx.datetime.DayOfWeek
import ru.bgitu.core.LessonPb
import ru.bgitu.core.SchedulePb

data class StoredSchedule(
    val firstWeek: Map<DayOfWeek, List<StoredLesson>>,
    val secondWeek: Map<DayOfWeek, List<StoredLesson>>
)

fun SchedulePb.toStoredSchedule(): StoredSchedule {
    return StoredSchedule(
        firstWeek = firstWeekMap.map { (isoDayNumber, day) ->
            DayOfWeek(isoDayNumber) to day.lessonsList.map(LessonPb::toStoredLesson)
        }.toMap(),
        secondWeek = secondWeekMap.map { (isoDayNumber, day) ->
            DayOfWeek(isoDayNumber) to day.lessonsList.map(LessonPb::toStoredLesson)
        }.toMap()
    )
}