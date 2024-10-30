package ru.bgitu.feature.notes.di

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module
import ru.bgitu.feature.notes.data.NotesRepository
import ru.bgitu.feature.notes.presentation.details.NoteDetailsViewModel
import ru.bgitu.feature.notes.presentation.list.NotesViewModel
import ru.bgitu.feature.notes.presentation.pane.Notes2PaneViewModel

val NotesModule = module {
    single {
        NotesRepository(
            noteDao = get()
        )
    }
    viewModelOf(::NoteDetailsViewModel)
    viewModelOf(::Notes2PaneViewModel)
    viewModelOf(::NotesViewModel)
}