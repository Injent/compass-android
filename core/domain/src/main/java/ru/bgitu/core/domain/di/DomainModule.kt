package ru.bgitu.core.domain.di

import org.koin.dsl.module
import ru.bgitu.core.domain.GetUserSettingsUseCase

val DomainModule = module {
    single { GetUserSettingsUseCase(get()) }
}