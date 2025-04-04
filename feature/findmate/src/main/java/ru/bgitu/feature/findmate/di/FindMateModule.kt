package ru.bgitu.feature.findmate.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.bgitu.feature.findmate.presentation.search.SearchMateViewModel

val FindMateModule = module {
    viewModel {
        SearchMateViewModel(
            compassRepository = get()
        )
    }
}