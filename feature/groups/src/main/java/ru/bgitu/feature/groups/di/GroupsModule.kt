package ru.bgitu.feature.groups.di

import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import ru.bgitu.feature.groups.data.GroupManagementRepository
import ru.bgitu.feature.groups.presentation.groups.GroupsViewModel
import ru.bgitu.feature.groups.presentation.search.GroupSearchViewModel

val GroupsModule = module {
    singleOf(::GroupManagementRepository)

    viewModelOf(::GroupsViewModel)

    viewModelOf(::GroupSearchViewModel)
}