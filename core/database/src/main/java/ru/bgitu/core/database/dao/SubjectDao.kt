package ru.bgitu.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ru.bgitu.core.database.entity.SubjectEntity

@Dao
interface SubjectDao {
    @Insert
    suspend fun insertAll(subjectEntities: List<SubjectEntity>)
    @Query("DELETE FROM Subject")
    suspend fun deleteTable()
}