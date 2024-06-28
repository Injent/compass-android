package ru.bgitu.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate
import ru.bgitu.core.database.entity.LessonEntity
import ru.bgitu.core.database.model.PopulatedLesson

@Dao
interface ScheduleDao {
    @Transaction
    @Query("SELECT * FROM Lesson WHERE date = :queryDate")
    suspend fun getClasses(queryDate: LocalDate): List<PopulatedLesson>

    @Transaction
    @Query("SELECT * FROM Lesson WHERE date = :queryDate")
    fun getLessonsStream(queryDate: LocalDate): Flow<List<PopulatedLesson>>

    @Transaction
    @Query("SELECT * FROM Lesson WHERE date >= :from AND date <= :to")
    fun getLessonsStream(from: LocalDate, to: LocalDate): Flow<List<PopulatedLesson>>

    @Transaction
    @Query("SELECT * FROM Lesson WHERE date >= :from AND date <= :to LIMIT :limit")
    fun getClasses(from: LocalDate, to: LocalDate, limit: Int): List<PopulatedLesson>

    @Insert
    suspend fun insertAllClasses(lessons: List<LessonEntity>)

    @Upsert
    suspend fun upsertLesson(lesson: LessonEntity)

    @Query("DELETE FROM Lesson WHERE date < :beforeDate")
    suspend fun deleteClassesBeforeDate(beforeDate: LocalDate)

    @Query("DELETE FROM Lesson")
    suspend fun deleteTable()
}