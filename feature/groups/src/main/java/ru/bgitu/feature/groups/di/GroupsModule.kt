package ru.bgitu.feature.groups.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.bgitu.feature.groups.data.GroupManagementRepository
import ru.bgitu.feature.groups.presentation.groups.GroupsViewModel
import ru.bgitu.feature.groups.presentation.search.GroupSearchViewModel

val GroupsModule = module {
    single {
        GroupManagementRepository(
            settingsRepository = get(),
            serviceApi = get()
        )
    }

    viewModel {
        GroupsViewModel(
            groupRepository = get(),
        )
    }

    viewModel {
        GroupSearchViewModel(
            groupRepository = get()
        )
    }
}