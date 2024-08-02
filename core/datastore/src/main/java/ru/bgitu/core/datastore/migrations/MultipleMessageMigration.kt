package ru.bgitu.core.datastore.migrations

import androidx.datastore.core.DataMigration
import kotlinx.serialization.json.Json
import ru.bgitu.core.datastore.SettingsPb
import ru.bgitu.core.datastore.copy
import ru.bgitu.core.datastore.credentialsPb
import ru.bgitu.core.datastore.dataVersionPb
import ru.bgitu.core.datastore.model.DeprecatedStudentInfo
import ru.bgitu.core.datastore.userDataPb
import ru.bgitu.core.datastore.userPrefsPb
import ru.bgitu.core.model.settings.UiTheme

class MultipleMessageMigration : DataMigration<SettingsPb> {
    override suspend fun cleanUp() = Unit

    override suspend fun migrate(currentData: SettingsPb): SettingsPb {
        return currentData.copy {
            // Migrate to DataVersion
            dataVersions = dataVersionPb {
                scheduleVersion = currentData.deprecatedScheduleVersion
                currAppVersionCode = currentData.deprecatedCurrAppVersionCode
                newFeaturesVersion = currentData.deprecatedNewFeaturesVersion
                schemaVersion = 1
            }

            // Migrate to UserPrefs
            prefs = userPrefsPb {
                ignoreMinorUpdates = false
                theme = UiTheme.entries[currentData.deprecatedUiTheme].toString()
                showPinnedSchedule = currentData.deprecatedShowPinnedSchedule
            }

            val json = Json {
                coerceInputValues = true
                ignoreUnknownKeys = true
            }
            val deprecatedInfo = runCatching {
                json.decodeFromString(
                    deserializer = DeprecatedStudentInfo.serializer(),
                    string = currentData.deprecatedStudentInfo
                )
            }.getOrNull()

            // Migrate to UserData
            if (deprecatedInfo != null) {
                userdata = userDataPb {
                    avatarUrl = deprecatedInfo.avatarUrl ?: ""
                    displayName = "${deprecatedInfo.name} ${deprecatedInfo.surname}"
                    role = deprecatedInfo.role.toString()
                }
            }

            // Migrate to Credentials
            credentials = credentialsPb {
                accessToken = currentData.deprecatedAccessToken
                refreshToken = currentData.deprecatedRefreshToken
                groupId = currentData.deprecatedGroupId
                groupName = deprecatedInfo?.groupName ?: ""
            }

            deprecatedStudentInfo = ""
            deprecatedScheduleVersion = -1
            deprecatedAccessToken = ""
            deprecatedExpirationDate = ""
            deprecatedRequestDate = ""
            deprecatedTimeDiff = -1
            deprecatedRefreshToken = ""
            deprecatedIgnoreMinorUpdates = false
            deprecatedUiTheme = -1
            deprecatedGroupId = -1
            deprecatedCurrAppVersionCode = -1
            deprecatedShowPinnedSchedule = false
            deprecatedLastForceUpdateVersion = -1
            deprecatedNewFeaturesVersion = -1
        }
    }

    override suspend fun shouldMigrate(currentData: SettingsPb): Boolean {
        return currentData.deprecatedCurrAppVersionCode != -1
    }
}