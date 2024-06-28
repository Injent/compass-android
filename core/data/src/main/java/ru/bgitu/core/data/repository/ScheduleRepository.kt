package ru.bgitu.core.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate
import ru.bgitu.core.data.model.ScheduleLoadState
import ru.bgitu.core.data.util.Syncable
import ru.bgitu.core.model.Lesson

interface ScheduleRepository : Syncable {
    fun getClassesStream(
        currentDate: LocalDate,
        preloadWeekCount: Int
    ): Flow<ScheduleLoadState>
    suspend fun getClasses(queryDate: LocalDate): List<Lesson>
    suspend fun getClasses(
        from: LocalDate,
        to: LocalDate,
        limit: Int = Int.MAX_VALUE
    ): List<Lesson>
    suspend fun getNetworkClasses(
        groupId: Int,
        from: LocalDate,
        to: LocalDate
    ): ScheduleLoadState
}