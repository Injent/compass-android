package ru.bgitu.core.datastore.migrations

import androidx.datastore.core.DataMigration
import ru.bgitu.core.SettingsPb

internal object DataStoreMigrations {
    internal val datastoreMigrations: List<DataMigration<SettingsPb>>
        get() = listOf(
            MigrationV1(),
            MigrationV2(),
            MigrationV3()
        )
}