package ru.bgitu.core.datastore

import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.bgitu.core.CredentialsPb
import ru.bgitu.core.DataVersionPb
import ru.bgitu.core.GroupSlotPb
import ru.bgitu.core.SettingsPb
import ru.bgitu.core.UserDataPb
import ru.bgitu.core.copy
import ru.bgitu.core.datastore.model.StoreVariants
import ru.bgitu.core.datastore.model.toDataStoreModel
import ru.bgitu.core.datastore.model.toExternalModel
import ru.bgitu.core.datastore.util.toKotlinType
import ru.bgitu.core.datastore.util.toProtobuf
import ru.bgitu.core.idOrNull
import ru.bgitu.core.metadataPb
import ru.bgitu.core.model.Contacts
import ru.bgitu.core.model.Group
import ru.bgitu.core.model.UserProfile
import ru.bgitu.core.model.UserRole
import ru.bgitu.core.model.settings.AppMetadata
import ru.bgitu.core.model.settings.UiTheme
import ru.bgitu.core.model.settings.UserCredentials
import ru.bgitu.core.model.settings.UserPrefs
import ru.bgitu.core.model.settings.UserSettings
import ru.bgitu.core.userPrefsPb

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
            userPrefs = getUserPrefs(it),
            isAuthorized = !it.metadata.isAnonymousUser && it.credentials.userId != 0L,
            userProfile = getProfile(it),
            isAnonymous = it.metadata.isAnonymousUser,
            shouldShowOnboarding = it.metadata.shouldShowOnboarding,
            shouldShowDataResetAlert = it.metadata.shouldShowDataResetAlert
        )
    }

    val credentials: Flow<UserCredentials?> = datastore.data.map {
        if (it.credentials.accessToken.isEmpty()) return@map null

        UserCredentials(
            accessToken = it.credentials.accessToken,
            refreshToken = it.credentials.refreshToken,
        )
    }

    val metadata: Flow<AppMetadata> = datastore.data.map {
        with(it.metadata) {
            AppMetadata(
                recentProfessorSearch = recentProfessorSearchList.distinct().asReversed(),
                newestUpdateChecksum = newestUpdateChecksum,
                seenTeacherScheduleAlert = seenTeacherScheduleAlert,
                availableVersionCode = availableVersionCode,
                scheduleNotifierAlarmDateTime = scheduleNotifierAlarmDateTime
                    .takeIf(String::isNotEmpty)?.toLocalDateTime(),
                isAnonymousUser = isAnonymousUser,
                shouldShowMateBanner = shouldShowMateBanner,
                shouldShowOnboarding = shouldShowOnboarding,
                shouldShowDataResetAlert = shouldShowDataResetAlert
            )
        }
    }

    suspend fun updateProfile(block: (UserProfile) -> UserProfile) {
        val new = getProfile()?.let { block(it) } ?: return

        datastore.updateData {
            it.copy {
                userdata = userdata.copy {
                    this.bio = new.bio
                    this.displayName = new.displayName
                    this.avatarUrl = new.avatarUrl ?: ""
                    this.tgUrl = new.contacts?.tg ?: ""
                    this.vkUrl = new.contacts?.vk ?: ""
                    this.publicProfile = new.publicProfile
                    this.role = new.userRole.name
                    this.variantsJson = json.encodeToString(
                        new.variants.map(UserProfile.VariantEntry::toDataStoreModel)
                    )
                }
            }
        }
    }

    private fun getProfile(data: SettingsPb): UserProfile? {
        return with(data) {
            if (credentials.userId == 0L) {
                return null
            }
            UserProfile(
                userId = credentials.userId,
                bio = userdata.bio,
                avatarUrl = userdata.avatarUrl.takeIf(String::isNotEmpty),
                displayName = userdata.displayName,
                contacts = if (userdata.tgUrl.isEmpty() || userdata.vkUrl.isEmpty()) {
                    null
                } else Contacts(tg = userdata.tgUrl, vk = userdata.vkUrl),
                variants = runCatching {
                    json.decodeFromString<List<StoreVariants>>(userdata.variantsJson)
                        .map(StoreVariants::toExternalModel)
                }.getOrElse { emptyList() },
                userRole = runCatching {
                    UserRole.valueOf(userdata.role)
                }.getOrDefault(UserRole.REGULAR),
                publicProfile = userdata.publicProfile
            )
        }
    }

    suspend fun getProfile(): UserProfile? {
        return getProfile(datastore.data.first())
    }

    suspend fun updateMetadata(block: (AppMetadata) -> AppMetadata) {
        val new = block(metadata.first())

        datastore.updateData {
            it.copy {
                metadata = metadata.copy {
                    recentProfessorSearch.clear()
                    recentProfessorSearch.addAll(
                        new.recentProfessorSearch.takeLast(MAX_RECENT_SEARCH_POOL)
                    )
                    newestUpdateChecksum = new.newestUpdateChecksum
                    seenTeacherScheduleAlert = new.seenTeacherScheduleAlert
                    availableVersionCode = new.availableVersionCode
                    scheduleNotifierAlarmDateTime =
                        new.scheduleNotifierAlarmDateTime?.toString() ?: ""
                    isAnonymousUser = new.isAnonymousUser
                    shouldShowMateBanner = new.shouldShowMateBanner
                    shouldShowOnboarding = new.shouldShowOnboarding
                    shouldShowDataResetAlert = new.shouldShowDataResetAlert
                }
            }
        }
    }

    suspend fun setAuthData(
        userId: Long,
        credentials: UserCredentials,
    ) {
        datastore.updateData {
            it.copy {
                this.credentials = this.credentials.copy {
                    this.userId = userId
                    accessToken = credentials.accessToken
                    refreshToken = credentials.refreshToken
                    lastAuthDate = Clock.System.now().toEpochMilliseconds()
                }
            }
        }
    }

    suspend fun getLastAuthDate(): Instant {
        return Instant.fromEpochMilliseconds(datastore.data.first().credentials.lastAuthDate)
    }

    suspend fun setCredentials(
        userCredentials: UserCredentials
    ) {
        datastore.updateData {
            it.copy {
                credentials = credentials.copy {
                    accessToken = userCredentials.accessToken
                    refreshToken = userCredentials.refreshToken
                    lastAuthDate = Clock.System.now().toEpochMilliseconds()
                }
            }
        }
    }

    suspend fun setGroup(group: Group) {
        datastore.updateData {
            it.copy {
                credentials = credentials.copy {
                    groupId = group.id
                    groupName = group.name
                }
                dataVersions = dataVersions.copy {
                    scheduleVersion = 0
                }
            }
        }
    }

    private fun getUserPrefs(data: SettingsPb): UserPrefs {
        return with(data.prefs) {
            UserPrefs(
                theme = UiTheme.valueOf(theme.ifEmpty { "SYSTEM" }),
                showPinnedSchedule = showPinnedSchedule,
                teacherFilterByDays = teacherSortByWeeks,
                savedGroups = savedGroupsList
                    .sortedBy { it.slotIndex }
                    .mapNotNull { groupSlotPb ->
                        if (groupSlotPb.idOrNull == null) return@mapNotNull null

                        Group(
                            id = groupSlotPb.id.toKotlinType(),
                            name = groupSlotPb.name.toKotlinType()
                        )
                    },
                showGroupsOnMainScreen = showGroupsOnMainScreen,
                helpSiteTraffic = helpSiteTraffic
            )
        }
    }

    suspend fun updateUserPrefs(block: (UserPrefs) -> UserPrefs) {
        val new = block(data.first().userPrefs)

        datastore.updateData {
            it.copy {
                prefs = it.prefs.copy {
                    theme = new.theme.toString()
                    showPinnedSchedule = new.showPinnedSchedule
                    teacherSortByWeeks = new.teacherFilterByDays
                    savedGroups.apply {
                        clear()
                        addAll(
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
                    helpSiteTraffic = new.helpSiteTraffic
                }
            }
        }
    }

    suspend fun getDataVersions(): DataVersions {
        return datastore.data.map {
            with(datastore.data.first().dataVersions) {
                DataVersions(
                    scheduleDataVersion = scheduleVersion,
                    newFeaturesVersion = newFeaturesVersion,
                    currentAppVersionCode = currAppVersionCode,
                    userDataVersion = userdataVersion
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
                    newFeaturesVersion = versions.newFeaturesVersion
                    currAppVersionCode = versions.currentAppVersionCode
                    userdataVersion = versions.userDataVersion
                }
            }
        }
    }

    suspend fun updateUserDataVersion(userDataVersion: Int, group: Group?) {
        datastore.updateData {
            it.copy {
                // Show reset data alert if group is not null
                metadata = metadata.copy {
                    shouldShowDataResetAlert = group == null
                }

                credentials = credentials.copy {
                    groupId = group?.id ?: 0
                    groupName = group?.name ?: ""
                }

                prefs = prefs.copy {
                    savedGroups.clear()
                }
                dataVersions = dataVersions.copy {
                    scheduleVersion = 0
                    this.userdataVersion = userDataVersion
                }
            }
        }
    }

    suspend fun clearUserData() {
        datastore.updateData {
            it.copy {
                credentials = CredentialsPb.getDefaultInstance()
                userdata = UserDataPb.getDefaultInstance()
                dataVersions = DataVersionPb.getDefaultInstance()
                val persistentPres = prefs
                prefs = userPrefsPb {
                    theme = persistentPres.theme
                }
                val persistentMetadata = metadata
                metadata = metadataPb {
                    newestUpdateChecksum = persistentMetadata.newestUpdateChecksum
                    availableVersionCode = persistentMetadata.availableVersionCode
                    isAnonymousUser = false
                }
            }
        }
    }
}