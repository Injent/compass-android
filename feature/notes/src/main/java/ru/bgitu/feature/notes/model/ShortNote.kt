package ru.bgitu.feature.notes.model

import kotlinx.datetime.LocalDateTime
import ru.bgitu.core.database.entity.ShortNoteModel

data class ShortNote(
    val id: Int,
    val shortName: String,
    val subjectName: String,
    val priority: Int,
    val completed: Boolean,
    val createDate: LocalDateTime
)

fun ShortNoteModel.toExternalModel() = ShortNote(
    id = id,
    shortName = shortName,
    subjectName = subjectName,
    priority = priority,
    completed = completed,
    createDate = createDate
)
