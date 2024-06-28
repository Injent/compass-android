package ru.bgitu.core.datastore

import androidx.annotation.VisibleForTesting
import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Instant
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonObject
import ru.bgitu.core.datastore.model.StoreVariants
import ru.bgitu.core.datastore.model.toDataStoreModel
import ru.bgitu.core.datastore.model.toExternalModel
import ru.bgitu.core.datastore.util.toKotlinType
import ru.bgitu.core.datastore.util.toProtobuf
import ru.bgitu.core.model.CompassAccount
import ru.bgitu.core.model.Contacts
import ru.bgitu.core.model.Group
import ru.bgitu.core.model.UserPermission
import ru.bgitu.core.model.UserProfile
import ru.bgitu.core.model.UserRole
import ru.bgitu.core.model.settings.AppMetadata
import ru.bgitu.core.model.settings.UiTheme
import ru.bgitu.core.model.settings.UserCredentials
import ru.bgitu.core.model.settings.UserPrefs
import ru.bgitu.core.model.settings.UserSettings

private const val MAX_RECENT_SEARCH_POOL = 10

class SettingsRepository(
    private val datastore: DataStore<SettingsPb>,
    private val json: Json,
) {

    val data: Flow<UserSettings> = datastore.data.map {
        UserSettings(
            userId = it.credentials.userId,
            groupId = it.credentials.groupId.takeIf { id -> id != 0 },
            groupName = it.credentials.groupName.takeIf { name -> name.isNotEmpty() },
            currentAppVersionCode = it.dataVersions.currAppVersionCode,
            newFeaturesVersion = it.dataVersions.newFeaturesVersion,
            userPrefs = getUserPrefs(),
            isAuthorized = it.credentials != null && it.credentials.groupId != 0,
            compassAccount = getAccountInfo(),
            userProfile = getProfile()
        )
    }

    val credentials: Flow<UserCredentials?> = datastore.data.map {
        if (it.credentials.accessToken.isEmpty()) return@map null

        UserCredentials(
            accessToken = it.credentials.accessToken,
            refreshToken = it.credentials.refreshToken,
            expirationDate = Instant.fromEpochMilliseconds(it.credentials.expirationDate),
            authDate = Instant.fromEpochMilliseconds(it.credentials.authDate),
        )
    }

    val metadata: Flow<AppMetadata> = datastore.data.map {
        with(it.metadata) {
            AppMetadata(
                lastStatisticsReportDate = Instant.fromEpochMilliseconds(lastStatisticsReport),
                appUuid = appUUID,
                recentProfessorSearch = recentProfessorSearchList.distinct().asReversed(),
                newestUpdateChecksum = newestUpdateChecksum,
                seenTeacherScheduleAlert = seenTeacherScheduleAlert,
                availableVersionCode = availableVersionCode,
                scheduleNotifierAlarmDateTime = scheduleNotifierAlarmDateTime.takeIf { date ->
                    date.isNotBlank()
                }?.toLocalDateTime()
            )
        }
    }

    suspend fun updateProfile(block: (UserProfile) -> UserProfile) {
        val new = block(getProfile())

        datastore.updateData {
            it.copy {
                userdata = userdata.copy {
                    this.bio = new.bio
                    this.lastName = new.lastName
                    this.name = new.firstName
                    this.avatarUrl = new.avatarUrl ?: ""
                    this.tgUrl = new.contacts?.tg ?: ""
                    this.vkUrl = new.contacts?.vk ?: ""
                    this.variantsJson = json.encodeToString(
                        new.variants.map(UserProfile.VariantEntry::toDataStoreModel)
                    )
                }
            }
        }
    }

    suspend fun getProfile(): UserProfile {
        return with(datastore.data.first()) {
            UserProfile(
                userId = credentials.userId,
                bio = userdata.bio,
                avatarUrl = userdata.avatarUrl.takeIf(String::isNotEmpty),
                lastName = userdata.lastName,
                firstName = userdata.name,
                contacts = if (userdata.tgUrl.isEmpty() || userdata.vkUrl.isEmpty()) {
                    null
                } else Contacts(tg = userdata.tgUrl, vk = userdata.vkUrl),
                variants = runCatching {
                    json.decodeFromString<List<StoreVariants>>(userdata.variantsJson)
                        .map(StoreVariants::toExternalModel)
                }.getOrElse { emptyList() },
                publicProfile = userdata.publicProfile
            )
        }
    }

    suspend fun updateMetadata(block: (AppMetadata) -> AppMetadata) {
        val new = block(metadata.first())

        datastore.updateData {
            it.copy {
                metadata = metadata.copy {
                    appUUID = new.appUuid
                    lastStatisticsReport = new.lastStatisticsReportDate.toEpochMilliseconds()
                    recentProfessorSearch.clear()
                    recentProfessorSearch.addAll(
                        new.recentProfessorSearch.takeLast(MAX_RECENT_SEARCH_POOL)
                    )
                    newestUpdateChecksum = new.newestUpdateChecksum
                    seenTeacherScheduleAlert = new.seenTeacherScheduleAlert
                    availableVersionCode = new.availableVersionCode
                    scheduleNotifierAlarmDateTime =
                        new.scheduleNotifierAlarmDateTime?.toString() ?: ""
                }
            }
        }
    }

    suspend fun setUserId(userId: Long) {
        datastore.updateData {
            it.copy {
                credentials = credentials.copy {
                    this.userId = userId
                }
            }
        }
    }

    private suspend fun getAccountInfo(): CompassAccount? {
        val settings = datastore.data.first()
        return with(settings.userdata) {
            if (lastName.isEmpty()) return null

            CompassAccount(
                userId = settings.credentials.userId,
                eosUserId = eosUserId,
                groupId = settings.credentials.groupId,
                groupName = settings.credentials.groupName,
                fullName = "$lastName $name",
                role = runCatching { UserRole.valueOf(role) }.getOrDefault(UserRole.STUDENT),
                avatarUrl = avatarUrl.takeIf { it.isNotEmpty() },
                permissions = permissionsList.map { UserPermission.valueOf(it) },
                data = buildJsonObject {}
            )
        }
    }

    suspend fun setCredentials(userCredentials: UserCredentials) {
        datastore.updateData {
            it.copy {
                credentials = credentials.copy {
                    accessToken = userCredentials.accessToken
                    refreshToken = userCredentials.refreshToken
                    expirationDate = userCredentials.expirationDate.toEpochMilliseconds()
                    authDate = userCredentials.authDate.toEpochMilliseconds()
                    dateDiff = userCredentials.measureDateDiff()
                }
            }
        }
    }

    suspend fun setGroup(group: Group) {
        datastore.updateData {
            it.copy {
                credentials = credentials.copy {
                    this.groupId = group.id
                    this.groupName = group.name
                }
            }
        }
    }

    suspend fun setAccountInfo(account: CompassAccount) {
        datastore.updateData {
            it.copy {
                credentials = credentials.copy {
                    userId = account.userId
                    groupId = account.groupId ?: 0
                    groupName = account.groupName ?: ""
                }
                userdata = userdata.copy {
                    eosUserId = account.eosUserId
                    lastName = account.fullName.split(" ").first()
                    name = account.fullName.split(" ").last()
                    avatarUrl = account.avatarUrl ?: ""
                    role = account.role.toString()
                    permissions.apply {
                        clear()
                        addAll(account.permissions.map(UserPermission::toString))
                    }
                }
            }
        }
    }

    private suspend fun getUserPrefs(): UserPrefs {
        return with(datastore.data.first().prefs) {
            UserPrefs(
                theme = UiTheme.valueOf(theme.ifEmpty { "SYSTEM" }),
                ignoreMinorUpdates = ignoreMinorUpdates,
                showPinnedSchedule = showPinnedSchedule,
                teacherSortByWeeks = teacherSortByWeeks,
                savedGroups = savedGroupsList
                    .sortedBy { it.slotIndex }
                    .mapNotNull { groupSlotPb ->
                        if (groupSlotPb.idOrNull == null) return@mapNotNull null

                        Group(
                            id = groupSlotPb.id.toKotlinType(),
                            name = groupSlotPb.name.toKotlinType()
                        )
                    },
                showGroupsOnMainScreen = showGroupsOnMainScreen
            )
        }
    }

    suspend fun updateUserPrefs(block: (UserPrefs) -> UserPrefs) {
        val new = block(data.first().userPrefs)

        datastore.updateData {
            it.copy {
                prefs = it.prefs.copy {
                    theme = new.theme.toString()
                    ignoreMinorUpdates = new.ignoreMinorUpdates
                    showPinnedSchedule = new.showPinnedSchedule
                    teacherSortByWeeks = new.teacherSortByWeeks
                    savedGroups.apply {
                        this.clear()
                        this.addAll(
                            new.savedGroups.mapIndexed { index, group ->
                                GroupSlotPb.newBuilder()
                                    .setSlotIndex(index)
                                    .setId(group.id.toProtobuf())
                                    .setName(group.name.toProtobuf())
                                    .build()
                            }
                        )
                    }
                    showGroupsOnMainScreen = new.showGroupsOnMainScreen
                }
            }
        }
    }

    suspend fun getDataVersions(): DataVersions {
        return datastore.data.map {
            with(datastore.data.first().dataVersions) {
                DataVersions(
                    scheduleDataVersion = scheduleVersion,
                    lastForceUpdateVersion = lastForceUpdateVersion,
                    newFeaturesVersion = newFeaturesVersion,
                    currentAppVersionCode = currAppVersionCode,
                    guestAccountVersion = guestAccountVersion
                )
            }
        }.first()
    }

    suspend fun updateDataVersions(block: (DataVersions) -> DataVersions) {
        datastore.updateData {
            val versions = block(getDataVersions())
            it.copy {
                dataVersions = dataVersions.copy {
                    scheduleVersion = versions.scheduleDataVersion
                    lastForceUpdateVersion = versions.lastForceUpdateVersion
                    newFeaturesVersion = versions.newFeaturesVersion
                    guestAccountVersion = versions.guestAccountVersion
                    currAppVersionCode = versions.currentAppVersionCode
                }
            }
        }
    }

    fun provideCredentials(): UserCredentials? {
        return runBlocking { credentials.first() }
    }

    suspend fun resetToDefaults() {
        datastore.updateData {
            it.copy {
                credentials = CredentialsPb.getDefaultInstance()
                userdata = UserDataPb.getDefaultInstance()
                dataVersions = dataVersions.copy {
                    scheduleVersion = 0
                }
                prefs = prefs.copy {
                    showPinnedSchedule = false
                }
            }
        }
    }

    suspend fun hasNoUserData(): Boolean {
        with(datastore.data.first()) {
            return arrayOf(
                credentials == CredentialsPb.getDefaultInstance(),
                userdata == UserDataPb.getDefaultInstance(),
                dataVersions.scheduleVersion == 0,
                prefs.showPinnedSchedule.not()
            ).all { it }
        }
    }
}