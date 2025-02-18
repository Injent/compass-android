package ru.bgitu.feature.notes.data

import kotlinx.coroutines.flow.mapLatest
import ru.bgitu.core.database.dao.NoteDao
import ru.bgitu.core.database.entity.ShortNoteModel
import ru.bgitu.feature.notes.model.Note
import ru.bgitu.feature.notes.model.toEntity
import ru.bgitu.feature.notes.model.toExternalModel

class NotesRepository(
    private val noteDao: NoteDao
) {
    val uncompletedNotes= noteDao.getAllNotesStream(onlyCompleted = false).mapLatest {
        it.map(ShortNoteModel::toExternalModel)
    }

    val completedNotes = noteDao.getAllNotesStream(onlyCompleted = true).mapLatest {
        it.map(ShortNoteModel::toExternalModel)
    }

    val deletedNotes = noteDao.getAllNotesStream(deleted = true).mapLatest {
        it.map(ShortNoteModel::toExternalModel)
    }

    suspend fun saveNote(note: Note): Int {
        return noteDao.upsertNote(note.toEntity()).toInt()
    }

    suspend fun setCompleted(noteId: Int, isCompleted: Boolean) {
        noteDao.setCompleted(noteId, isCompleted)
    }

    suspend fun putIntoBin(noteId: Int) {
        noteDao.setAsDeleted(noteId = noteId, deleted = true)
    }

    suspend fun delete(noteId: Int) {
        noteDao.deleteNote(noteId)
    }

    suspend fun getNote(noteId: Int): Note {
        return noteDao.getNote(noteId).toExternalModel()
    }
}