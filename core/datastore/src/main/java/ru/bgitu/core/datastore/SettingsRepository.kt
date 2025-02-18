package ru.bgitu.core.datastore

import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.json.Json
import ru.bgitu.core.DayPb
import ru.bgitu.core.GroupSlotPb
import ru.bgitu.core.LessonPb
import ru.bgitu.core.SchedulePb
import ru.bgitu.core.SettingsPb
import ru.bgitu.core.copy
import ru.bgitu.core.datastore.model.StoreVariants
import ru.bgitu.core.datastore.model.StoredLesson
import ru.bgitu.core.datastore.model.StoredSchedule
import ru.bgitu.core.datastore.model.toExternalModel
import ru.bgitu.core.datastore.model.toStoredSchedule
import ru.bgitu.core.datastore.util.toKotlinType
import ru.bgitu.core.datastore.util.toProtobuf
import ru.bgitu.core.idOrNull
import ru.bgitu.core.model.Contacts
import ru.bgitu.core.model.Group
import ru.bgitu.core.model.UserProfile
import ru.bgitu.core.model.UserRole
import ru.bgitu.core.model.settings.AppMetadata
import ru.bgitu.core.model.settings.SubscribedTopic
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
            userPrefs = getUserPrefs(it),
            isAuthorized = !it.metadata.isAnonymousUser && it.credentials.userId != 0L,
            userProfile = getProfile(it),
            isAnonymous = it.metadata.isAnonymousUser,
            shouldShowOnboarding = it.metadata.shouldShowOnboarding,
            shouldShowDataResetAlert = it.metadata.shouldShowDataResetAlert,
        )
    }

    val unseenFeatures = datastore.data.map { it.metadata.unseenFeatureIdsList.toList() }

    val schedule: Flow<StoredSchedule> = datastore.data.map {
        it.schedule.toStoredSchedule()
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
                shouldShowDataResetAlert = shouldShowDataResetAlert,
                unseenFeatureIds = unseenFeatureIdsList.toList(),
                isPushMessagesInitialized = isPushMessagesInitialized,
                messagingToken = messagingToken
            )
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
                    isPushMessagesInitialized = new.isPushMessagesInitialized
                    messagingToken = new.messagingToken
                    unseenFeatureIds.clear()
                    unseenFeatureIds.addAll(new.unseenFeatureIds)
                }
            }
        }
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
                helpSiteTraffic = helpSiteTraffic,
                useDynamicTheme = useDynamicTheme,
                subscribedTopics = subscribedTopicsList.map(SubscribedTopic::valueOf),
                notificationDelegationEnabled = notificationDelegationEnabled
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
                    showGroupsOnMainScreen = new.showGroupsOnMainScreen
                    helpSiteTraffic = new.helpSiteTraffic
                    useDynamicTheme = new.useDynamicTheme
                    notificationDelegationEnabled = new.notificationDelegationEnabled
                    subscribedTopics.apply {
                        clear()
                        addAll(new.subscribedTopics.map(SubscribedTopic::name))
                    }
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

    suspend fun setSchedule(schedule: StoredSchedule) {
        datastore.updateData {
            it.copy {
                val scheduleBuilder = SchedulePb.newBuilder()

                schedule.firstWeek.forEach { (dayOfWeek, storedLessons) ->
                    scheduleBuilder.putFirstWeek(
                        dayOfWeek.isoDayNumber, storedLessons.toDayPb()
                    )
                }

                schedule.secondWeek.forEach { (dayOfWeek, storedLessons) ->
                    scheduleBuilder.putSecondWeek(
                        dayOfWeek.isoDayNumber, storedLessons.toDayPb()
                    )
                }

                this.schedule = scheduleBuilder.build()
            }
        }
    }

    suspend fun clearSchedule() {
        datastore.updateData {
            it.copy {
                schedule = SchedulePb.getDefaultInstance()
            }
        }
    }
}

private fun StoredLesson.toProtobuf(): LessonPb = LessonPb.newBuilder()
    .setSubjectId(subjectId)
    .setSubjectName(subjectName)
    .setBuilding(building)
    .setStartAt(startAt.toSecondOfDay())
    .setEndAt(endAt.toSecondOfDay())
    .setClassroom(classroom)
    .setTeacher(teacher)
    .setIsLecture(isLecture)
    .build()

private fun List<StoredLesson>.toDayPb(): DayPb {
    return DayPb.newBuilder()
        .addAllLessons(this.map(StoredLesson::toProtobuf))
        .build()
}