package ru.bgitu.core.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.datetime.LocalDate
import ru.bgitu.core.common.CommonStrings
import ru.bgitu.core.common.DateTimeUtil.isOddWeek
import ru.bgitu.core.common.TextResource
import ru.bgitu.core.common.getOrElse
import ru.bgitu.core.data.model.ScheduleState
import ru.bgitu.core.data.model.toStoredSchedule
import ru.bgitu.core.data.util.Synchronizer
import ru.bgitu.core.datastore.SettingsRepository
import ru.bgitu.core.datastore.model.StoredLesson
import ru.bgitu.core.model.Lesson
import ru.bgitu.core.network.CompassService

class FirstOfflineScheduleRepository(
    private val serviceApi: CompassService,
    private val settingsRepository: SettingsRepository,
) : ScheduleRepository {
    @Suppress("USELESS_CAST")
    override val schedule: Flow<ScheduleState> = settingsRepository.schedule.map { storedSchedule ->
        ScheduleState.Loaded(storedSchedule) as ScheduleState
    }
        .catch {
            ScheduleState.Error(details = TextResource.Id(CommonStrings.error_unknown)).also { emit(it) }
        }
        .onStart {
            emit(ScheduleState.Loading)
        }

    override suspend fun getNetworkLessons(groupId: Int): ScheduleState {
        val schedule = serviceApi.getFullSchedule(groupId, true)
            .getOrElse { e ->
                e.throwable?.printStackTrace()
                return ScheduleState.Error(e.details)
            }
        return ScheduleState.Loaded(schedule.toStoredSchedule())
    }

    override fun getNetworkLessonsFlow(groupId: Int): Flow<ScheduleState> = flow {
        emit(getNetworkLessons(groupId))
    }
        .onStart { emit(ScheduleState.Loading) }

    override suspend fun getLessonsForDate(date: LocalDate): List<StoredLesson> {
        val schedule = settingsRepository.schedule.first()

        return if (date.isOddWeek()) {
            schedule.firstWeek
        } else {
            schedule.secondWeek
        }.getOrDefault(date.dayOfWeek, emptyList())
    }

    override suspend fun getClasses(from: LocalDate, to: LocalDate, limit: Int): List<Lesson> {
        return emptyList()
    }

    override suspend fun syncWith(isManualSync: Boolean, synchronizer: Synchronizer): Boolean {
        val group = settingsRepository.data.first().primaryGroup ?: return false
        val dataVersion = synchronizer.dataVersions()

        val remoteUserDataVersion = serviceApi.getUserDataVersion()
            .getOrElse { return false }.userDataVersion

        // Set initial value from server for client
        if (dataVersion.userDataVersion == 0) {
            settingsRepository.updateDataVersions {
                it.copy(userDataVersion = remoteUserDataVersion)
            }
        }

        if (remoteUserDataVersion != dataVersion.userDataVersion) {
            settingsRepository.updateUserDataVersion(
                userDataVersion = remoteUserDataVersion,
                group = group.takeIf { isManualSync }
            )
            if (!isManualSync) {
                settingsRepository.clearSchedule()
                return true
            }
        }

        val remoteDataVersion = serviceApi.getScheduleVersion(group.id)
            .getOrElse { return false }

        if (dataVersion.scheduleDataVersion == remoteDataVersion.scheduleVersion) {
            return true
        }

        val response = serviceApi.getFullSchedule(group.id)
            .getOrElse {
                it.throwable?.printStackTrace()
                return false
            }

        settingsRepository.setSchedule(response.toStoredSchedule())

        synchronizer.updateChangeListVersions {
            copy(scheduleDataVersion = remoteDataVersion.scheduleVersion)
        }
        return true
    }
}