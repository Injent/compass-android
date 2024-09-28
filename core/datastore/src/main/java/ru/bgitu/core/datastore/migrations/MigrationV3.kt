package ru.bgitu.core.datastore.migrations

import androidx.datastore.core.DataMigration
import ru.bgitu.core.SettingsPb
import ru.bgitu.core.copy

class MigrationV3 : DataMigration<SettingsPb> {
    override suspend fun cleanUp() = Unit

    override suspend fun migrate(currentData: SettingsPb): SettingsPb {
        return currentData.copy {
            dataVersions = dataVersions.copy {
                schemaVersion = 3
            }
            prefs = prefs.copy {
                helpSiteTraffic = true
            }
        }
    }

    override suspend fun shouldMigrate(currentData: SettingsPb): Boolean =
        currentData.dataVersions.schemaVersion < 3
}