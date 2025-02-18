package ru.bgitu.core.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate
import ru.bgitu.core.data.model.ScheduleState
import ru.bgitu.core.data.util.Syncable
import ru.bgitu.core.datastore.model.StoredLesson
import ru.bgitu.core.model.Lesson

interface ScheduleRepository : Syncable {
    val schedule: Flow<ScheduleState>
    suspend fun getNetworkLessons(groupId: Int): ScheduleState
    fun getNetworkLessonsFlow(groupId: Int): Flow<ScheduleState>
    suspend fun getLessonsForDate(date: LocalDate): List<StoredLesson>

    suspend fun getClasses(
        from: LocalDate,
        to: LocalDate,
        limit: Int = Int.MAX_VALUE
    ): List<Lesson>
}