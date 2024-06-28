package ru.bgitu.feature.about.di

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import ru.bgitu.core.common.versionCode
import ru.bgitu.feature.about.presentation.AboutViewModel

val AboutModule = module {
    factory {
        AboutViewModel(
            compassRepository = get(),
            currentVersionCode = androidContext().versionCode
        )
    }
}