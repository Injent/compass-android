package ru.bgitu.core.datastore.migrations

import androidx.datastore.core.DataMigration
import kotlinx.datetime.Instant
import kotlinx.serialization.json.Json
import ru.bgitu.core.datastore.SettingsPb
import ru.bgitu.core.datastore.copy
import ru.bgitu.core.datastore.credentialsPb
import ru.bgitu.core.datastore.dataVersionPb
import ru.bgitu.core.datastore.metadataPb
import ru.bgitu.core.datastore.model.DeprecatedStudentInfo
import ru.bgitu.core.datastore.userDataPb
import ru.bgitu.core.datastore.userPrefsPb
import ru.bgitu.core.model.UserPermission
import ru.bgitu.core.model.settings.UiTheme
import java.util.UUID

class MultipleMessageMigration : DataMigration<SettingsPb> {
    override suspend fun cleanUp() = Unit

    override suspend fun migrate(currentData: SettingsPb): SettingsPb {
        return currentData.copy {
            // Migrate to DataVersion
            dataVersions = dataVersionPb {
                scheduleVersion = currentData.deprecatedScheduleVersion
                lastForceUpdateVersion = currentData.deprecatedLastForceUpdateVersion
                currAppVersionCode = currentData.deprecatedCurrAppVersionCode
                newFeaturesVersion = currentData.deprecatedNewFeaturesVersion
                schemaVersion = 1
                guestAccountVersion = 1
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
                    lastName = deprecatedInfo.surname
                    name = deprecatedInfo.name
                    permissions.apply {
                        clear()
                        addAll(deprecatedInfo.permissions.map(UserPermission::toString))
                    }
                    role = deprecatedInfo.role.toString()
                    eosUserId = deprecatedInfo.studentId.toLong()
                }
            }

            // Migrate to Credentials
            credentials = credentialsPb {
                accessToken = currentData.deprecatedAccessToken
                refreshToken = currentData.deprecatedRefreshToken
                expirationDate = Instant.DISTANT_PAST.toEpochMilliseconds()
                authDate = Instant.DISTANT_PAST.toEpochMilliseconds()
                dateDiff = currentData.deprecatedTimeDiff
                groupId = currentData.deprecatedGroupId
                groupName = deprecatedInfo?.groupName ?: ""
                isVerified = deprecatedInfo != null
            }

            metadata = metadataPb {
                appUUID = UUID.randomUUID().toString()
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