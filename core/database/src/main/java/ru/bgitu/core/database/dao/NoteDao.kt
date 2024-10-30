package ru.bgitu.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import ru.bgitu.core.database.entity.NoteEntity
import ru.bgitu.core.database.entity.ShortNoteModel

@Dao
interface NoteDao {
    @Query(
        """
        SELECT id, subject_name, create_date, priority, is_completed,
            CASE
                WHEN LENGTH(content) > 50 THEN SUBSTR(REPLACE(content, '\n', ' '), 1, 50) || '...'
                ELSE content
            END as content
        FROM notes WHERE is_deleted = :deleted AND is_completed = :onlyCompleted ORDER BY priority, create_date DESC
    """
    )
    fun getAllNotesStream(onlyCompleted: Boolean? = null, deleted: Boolean = false): Flow<List<ShortNoteModel>>

    @Query("SELECT * FROM notes WHERE id = :id LIMIT 1")
    suspend fun getNote(id: Int): NoteEntity

    @Upsert
    suspend fun upsertNote(note: NoteEntity): Long

    @Query("UPDATE notes SET is_deleted = :deleted WHERE id = :noteId")
    suspend fun setDeleted(noteId: Int, deleted: Boolean)

    @Query("DELETE FROM notes WHERE id = :noteId")
    suspend fun deleteNote(noteId: Int)

    @Query("UPDATE notes SET is_completed = :isCompleted WHERE id = :noteId")
    suspend fun setCompleted(noteId: Int, isCompleted: Boolean)
}