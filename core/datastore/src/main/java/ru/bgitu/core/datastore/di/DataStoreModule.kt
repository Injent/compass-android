package ru.bgitu.core.datastore.di

import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.dataStoreFile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import ru.bgitu.core.SettingsPb
import ru.bgitu.core.datastore.SettingsRepository
import ru.bgitu.core.datastore.SettingsSerializer
import ru.bgitu.core.datastore.migrations.MigrationV1
import ru.bgitu.core.datastore.migrations.MigrationV2
import ru.bgitu.core.datastore.migrations.MigrationV3
import ru.bgitu.core.datastore.migrations.MigrationV4

private const val DATASTORE_FILE = "settings.pb"

val DatastoreModule = module {
    single<DataStore<SettingsPb>> {
        DataStoreFactory.create(
            serializer = SettingsSerializer,
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
            corruptionHandler = ReplaceFileCorruptionHandler {
                SettingsPb.getDefaultInstance()
            },
            migrations = listOf(
                MigrationV1(),
                MigrationV2(),
                MigrationV3(),
                MigrationV4(),
            )
        ) {
            androidContext().dataStoreFile(DATASTORE_FILE)
        }
    }

    single<SettingsRepository> {
        SettingsRepository(
            datastore = get(),
            json = get(),
        )
    }
}