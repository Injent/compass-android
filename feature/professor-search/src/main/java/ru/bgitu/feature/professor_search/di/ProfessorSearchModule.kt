package ru.bgitu.feature.professor_search.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.bgitu.feature.professor_search.presentation.details.ProfessorDetailsViewModel
import ru.bgitu.feature.professor_search.presentation.search.ProfessorSearchViewModel

val ProfessorSearchModule = module {
    viewModel { params ->
        ProfessorSearchViewModel(
            compassRepository = get(),
            settingsRepository = get(),
            professorName = params.getOrNull()
        )
    }
    viewModel {
        ProfessorDetailsViewModel(
            compassRepository = get(),
            professorNameArg = it.get(),
            settingsRepository = get()
        )
    }
}