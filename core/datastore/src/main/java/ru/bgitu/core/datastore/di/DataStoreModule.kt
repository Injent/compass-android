package ru.bgitu.core.datastore.di

import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.dataStoreFile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.runBlocking
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import ru.bgitu.core.SettingsPb
import ru.bgitu.core.UserDataPb
import ru.bgitu.core.UserPrefsPb
import ru.bgitu.core.copy
import ru.bgitu.core.datastore.SettingsRepository
import ru.bgitu.core.datastore.SettingsSerializer
import ru.bgitu.core.datastore.migrations.DataStoreMigrations
import ru.bgitu.core.model.UserRole
import ru.bgitu.core.model.settings.UiTheme
import ru.bgitu.core.model.settings.UserPrefs
import ru.bgitu.core.userDataPb
import ru.bgitu.core.userPrefsPb

private const val DATASTORE_FILE = "settings.pb"

val DatastoreModule = module {
    single<SettingsRepository> {
        val dataStore = DataStoreFactory.create(
            serializer = SettingsSerializer,
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
            corruptionHandler = ReplaceFileCorruptionHandler {
                SettingsPb.getDefaultInstance()
            },
            migrations = DataStoreMigrations.datastoreMigrations
        ) {
            androidContext().dataStoreFile(DATASTORE_FILE)
        }
        SettingsRepository(
            datastore = dataStore,
            json = get(),
        )
    }
}