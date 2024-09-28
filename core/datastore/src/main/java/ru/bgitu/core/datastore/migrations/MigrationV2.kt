package ru.bgitu.core.datastore.migrations

import androidx.datastore.core.DataMigration
import ru.bgitu.core.SettingsPb
import ru.bgitu.core.copy
import ru.bgitu.core.model.UserRole
import ru.bgitu.core.model.settings.UiTheme
import ru.bgitu.core.userDataPb

class MigrationV2 : DataMigration<SettingsPb> {
    override suspend fun cleanUp() = Unit

    override suspend fun migrate(currentData: SettingsPb): SettingsPb {
        return currentData.copy {
            dataVersions = dataVersions.copy {
                schemaVersion = 2
            }
            userdata = userDataPb {
                role = UserRole.REGULAR.name
                publicProfile = true
            }
            prefs = prefs.copy {
                if (theme.isEmpty()) {
                    theme = UiTheme.SYSTEM.name
                }
                showGroupsOnMainScreen = true
                teacherSortByWeeks = true
            }
            credentials = credentials.copy {
                accessToken = ""
                refreshToken = ""
                lastAuthDate = 0
                userId = 0
            }
            metadata = metadata.copy {
                shouldShowOnboarding = false
                shouldShowMateBanner = false
                isAnonymousUser = true
            }
        }
    }

    override suspend fun shouldMigrate(currentData: SettingsPb): Boolean =
        currentData.dataVersions.schemaVersion < 2
}