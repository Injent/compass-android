package ru.bgitu.core.database.entity

import androidx.room.ColumnInfo
import kotlinx.datetime.LocalDateTime

data class ShortNoteModel(
    @ColumnInfo("id") val id: Int,
    @ColumnInfo("content") val shortName: String,
    @ColumnInfo("subject_name") val subjectName: String,
    @ColumnInfo("priority") val priority: Int,
    @ColumnInfo("create_date") val createDate: LocalDateTime,
    @ColumnInfo("is_completed") val completed: Boolean
)