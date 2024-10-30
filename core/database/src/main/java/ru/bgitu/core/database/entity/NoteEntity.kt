package ru.bgitu.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo("id") val id: Int? = null,
    @ColumnInfo("subject_name") val subjectName: String,
    @ColumnInfo("content") val content: String,
    @ColumnInfo("priority") val priority: Int = 0,
    @ColumnInfo("create_date") val createDate: LocalDateTime = Clock.System.now().toLocalDateTime(
        TimeZone.currentSystemDefault()),
    @ColumnInfo("is_completed") val isCompleted: Boolean = false,
    @ColumnInfo("is_deleted") val isDeleted: Boolean = false,
    @ColumnInfo("complete_before_date") val completeBeforeDate: LocalDateTime? = null
)