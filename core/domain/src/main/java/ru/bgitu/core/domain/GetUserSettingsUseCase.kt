package ru.bgitu.core.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import ru.bgitu.core.datastore.SettingsRepository
import ru.bgitu.core.model.settings.UserSettings

class GetUserSettingsUseCase(
    private val settings: SettingsRepository
) {
    operator fun invoke(): Flow<UserSettings> = settings.data
    suspend fun get(): UserSettings = settings.data.first()
}