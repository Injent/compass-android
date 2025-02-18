package ru.bgitu.core.datastore.migrations

import android.content.Context
import androidx.datastore.core.DataMigration
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import ru.bgitu.core.SettingsPb
import ru.bgitu.core.copy
import ru.bgitu.core.datastore.model.UnseenFeatures

class MigrationV4 : DataMigration<SettingsPb>, KoinComponent {
    override suspend fun cleanUp() {
        runCatching {
            get<Context>().deleteDatabase("bgitu-database")
        }
    }

    override suspend fun migrate(currentData: SettingsPb): SettingsPb {
        return currentData.copy {
            dataVersions = dataVersions.copy {
                schemaVersion = 4
                scheduleVersion = 0
            }
            prefs = prefs.copy {
                teacherSortByWeeks = true
            }
            widgetState = widgetState.copy {
                options = options.copy {
                    opacity = 1f
                }
            }
            metadata = metadata.copy {
                unseenFeatureIds.addAll(
                    listOf(
                        UnseenFeatures.NEW_CHANGELOG,
                        UnseenFeatures.ADD_WIDGET,
                    )
                )
            }
        }
    }

    override suspend fun shouldMigrate(currentData: SettingsPb) =
        currentData.dataVersions.schemaVersion < 4
}