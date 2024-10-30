package ru.bgitu.feature.notes.model

import kotlinx.datetime.LocalDateTime
import ru.bgitu.core.database.entity.NoteEntity

data class Note(
    val id: Int?,
    val subjectName: String,
    val content: String,
    val priority: Int,
    val createDate: LocalDateTime,
    val isCompleted: Boolean,
    val completeBeforeDate: LocalDateTime?
)

fun NoteEntity.toExternalModel() = Note(
    id = id!!,
    subjectName = subjectName,
    content = content,
    priority = priority,
    createDate = createDate,
    isCompleted = isCompleted,
    completeBeforeDate = completeBeforeDate
)

fun Note.toEntity() = NoteEntity(
    id = id,
    subjectName = subjectName,
    content = content,
    priority = priority,
    createDate = createDate,
    isCompleted = isCompleted,
    completeBeforeDate = completeBeforeDate
)