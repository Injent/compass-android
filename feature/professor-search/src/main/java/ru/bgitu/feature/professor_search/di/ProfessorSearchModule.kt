package ru.bgitu.feature.professor_search.di

import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import ru.bgitu.feature.professor_search.presentation.details.TeacherDetailsViewModel
import ru.bgitu.feature.professor_search.presentation.search.TeacherSearchViewModel

val ProfessorSearchModule = module {
    viewModelOf(::TeacherSearchViewModel)
    viewModel { params ->
        TeacherDetailsViewModel(
            compassRepository = get(),
            settingsRepository = get(),
            teacherName = checkNotNull(params.getOrNull()) {
                "Teacher name not provided in ViewModel params"
            },
            networkMonitor = get()
        )
    }
}