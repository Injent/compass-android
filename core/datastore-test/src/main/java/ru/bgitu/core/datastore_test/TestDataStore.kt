package ru.bgitu.core.datastore_test

import androidx.datastore.core.DataStoreFactory
import kotlinx.coroutines.CoroutineScope
import org.junit.rules.TemporaryFolder
import ru.bgitu.core.datastore.SettingsSerializer
import java.io.File

fun TemporaryFolder.testSettingsDataStore(
    coroutineScope: CoroutineScope,
) = DataStoreFactory.create(
    serializer = SettingsSerializer,
    scope = coroutineScope,
) {
    newFile("settings_test.pb")
}
