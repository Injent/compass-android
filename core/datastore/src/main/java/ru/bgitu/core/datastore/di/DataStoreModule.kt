package ru.bgitu.core.datastore.di

import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import ru.bgitu.core.datastore.SettingsPb
import ru.bgitu.core.datastore.SettingsRepository
import ru.bgitu.core.datastore.SettingsSerializer
import ru.bgitu.core.datastore.migrations.DataStoreMigrations
import java.io.File

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
            File(androidContext().dataDir, DATASTORE_FILE)
        }
        SettingsRepository(
            datastore = dataStore,
            json = get(),
        )
    }
}