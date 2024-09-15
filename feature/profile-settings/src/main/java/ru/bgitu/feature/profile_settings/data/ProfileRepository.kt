package ru.bgitu.feature.profile_settings.data

import ru.bgitu.core.datastore.SettingsRepository
import ru.bgitu.core.network.CompassService

class ProfileRepository(
    private val settingsRepository: SettingsRepository,
    private val compassService: CompassService
)