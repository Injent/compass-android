package ru.bgitu.feature.professor_search.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.bgitu.feature.professor_search.presentation.details.TeacherDetailsViewModel
import ru.bgitu.feature.professor_search.presentation.search.TeacherSearchViewModel

val ProfessorSearchModule = module {
    viewModel {
        TeacherSearchViewModel(
            compassRepository = get(),
            settingsRepository = get(),
        )
    }
    viewModel { params ->
        TeacherDetailsViewModel(
            compassRepository = get(),
            settingsRepository = get(),
            teacherName = checkNotNull(params.getOrNull()) {
                "Teacher name not provided in ViewModel params"
            },
        )
    }
}