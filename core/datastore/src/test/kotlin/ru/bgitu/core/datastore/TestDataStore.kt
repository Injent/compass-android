package ru.bgitu.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import kotlinx.coroutines.CoroutineScope
import org.junit.rules.TemporaryFolder
import ru.bgitu.core.datastore.migrations.DataStoreMigrations

fun TemporaryFolder.testDataStore(
    coroutineScope: CoroutineScope,
): DataStore<SettingsPb> {
    return DataStoreFactory.create(
        serializer = SettingsSerializer,
        scope = coroutineScope,
        corruptionHandler = ReplaceFileCorruptionHandler {
            SettingsPb.getDefaultInstance()
        },
        migrations = DataStoreMigrations.datastoreMigrations
    ) {
        newFile("datastore_test.pb")
    }
}