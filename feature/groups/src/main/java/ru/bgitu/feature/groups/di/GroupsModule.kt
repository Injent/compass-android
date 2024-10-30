package ru.bgitu.feature.groups.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module
import ru.bgitu.feature.groups.data.GroupManagementRepository
import ru.bgitu.feature.groups.presentation.groups.GroupsViewModel
import ru.bgitu.feature.groups.presentation.search.GroupSearchViewModel

val GroupsModule = module {
    single {
        GroupManagementRepository(
            settingsRepository = get(),
            compassService = get()
        )
    }

    viewModel {
        GroupsViewModel(
            groupRepository = get(),
        )
    }

    viewModelOf(::GroupSearchViewModel)
}