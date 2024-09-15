package ru.bgitu.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate
import ru.bgitu.core.database.entity.LessonEntity

@Dao
interface ScheduleDao {
    @Transaction
    @Query("SELECT * FROM lessons WHERE date = :queryDate")
    suspend fun getClasses(queryDate: LocalDate): List<LessonEntity>

    @Transaction
    @Query("SELECT * FROM lessons WHERE date = :queryDate")
    fun getLessonsStream(queryDate: LocalDate): Flow<List<LessonEntity>>

    @Transaction
    @Query("SELECT * FROM lessons WHERE date >= :from AND date <= :to")
    fun getLessonsStream(from: LocalDate, to: LocalDate): Flow<List<LessonEntity>>

    @Transaction
    @Query("SELECT * FROM lessons WHERE date >= :from AND date <= :to LIMIT :limit")
    fun getClasses(from: LocalDate, to: LocalDate, limit: Int): List<LessonEntity>

    @Insert
    suspend fun insertAllClasses(lessons: List<LessonEntity>)

    @Upsert
    suspend fun upsertLesson(lesson: LessonEntity)

    @Query("DELETE FROM lessons WHERE date < :beforeDate")
    suspend fun deleteClassesBeforeDate(beforeDate: LocalDate)

    @Query("DELETE FROM lessons")
    suspend fun deleteTable()
}