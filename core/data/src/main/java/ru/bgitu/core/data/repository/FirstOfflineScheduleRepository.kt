package ru.bgitu.core.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEmpty
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import ru.bgitu.core.common.CommonStrings
import ru.bgitu.core.common.DateTimeUtil
import ru.bgitu.core.common.TextResource
import ru.bgitu.core.common.getOrElse
import ru.bgitu.core.data.model.ScheduleLoadState
import ru.bgitu.core.data.model.toEntity
import ru.bgitu.core.data.model.toExternalModel
import ru.bgitu.core.data.util.Synchronizer
import ru.bgitu.core.database.dao.ScheduleDao
import ru.bgitu.core.database.entity.LessonEntity
import ru.bgitu.core.datastore.SettingsRepository
import ru.bgitu.core.model.Lesson
import ru.bgitu.core.network.CompassService
import ru.bgitu.core.network.model.NetworkLesson

class FirstOfflineScheduleRepository(
    private val serviceApi: CompassService,
    private val settingsRepository: SettingsRepository,
    private val scheduleDao: ScheduleDao,
) : ScheduleRepository {
    override fun getClassesStream(currentDate: LocalDate, preloadWeekCount: Int): Flow<ScheduleLoadState> {
        return scheduleDao.getLessonsStream(
            DateTimeUtil.getStartOfWeek(currentDate).minus(preloadWeekCount, DateTimeUnit.WEEK),
            DateTimeUtil.getEndOfWeek(currentDate).plus(preloadWeekCount, DateTimeUnit.WEEK)
        )
            .map<List<LessonEntity>, ScheduleLoadState> {
                if (it.isEmpty()) return@map ScheduleLoadState.Success(emptyMap())

                val lessons = it.map(LessonEntity::toExternalModel)
                ScheduleLoadState.Success(
                    data = lessons.groupBy { lesson -> lesson.date }.toMap()
                )
            }
            .onEmpty {
                emit(ScheduleLoadState.Success(emptyMap()))
            }
            .catch {
                emit(ScheduleLoadState.Error(TextResource.Id(CommonStrings.error_unknown)))
            }
    }

    override suspend fun getNetworkClasses(
        groupId: Int,
        from: LocalDate,
        to: LocalDate,
    ): ScheduleLoadState {
        val lessons = serviceApi.getSchedule(
            groupId = groupId,
            fromDate = from,
            toDate = to,
            userDataVersion = settingsRepository.getDataVersions().userDataVersion
        )
            .getOrElse { return ScheduleLoadState.Error(it.details) }

        return ScheduleLoadState.Success(
            data = lessons
                .map(NetworkLesson::toExternalModel)
                .groupBy { lesson ->
                    lesson.date
                }
                .toMap()
        )
    }

    override suspend fun getClasses(queryDate: LocalDate): List<Lesson> {
        return scheduleDao.getClasses(queryDate).map(LessonEntity::toExternalModel)
    }

    override suspend fun getClasses(from: LocalDate, to: LocalDate, limit: Int): List<Lesson> {
        return scheduleDao.getClasses(from, to, limit).map(LessonEntity::toExternalModel)
    }

    override suspend fun syncWith(isManualSync: Boolean, synchronizer: Synchronizer): Boolean {
        val group = settingsRepository.data.first().primaryGroup ?: return false
        val dataVersion = synchronizer.dataVersions()

        val remoteUserDataVersion = serviceApi.getUserDataVersion().getOrElse { return false }

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
                scheduleDao.deleteTable()
                return true
            }
        }

        val remoteDataVersion = serviceApi.getScheduleVersion(group.id)
            .getOrElse { return false }

        if (dataVersion.scheduleDataVersion == remoteDataVersion.scheduleVersion) {
            return true
        }

        val now = DateTimeUtil.currentDate
        val fromDate = DateTimeUtil.getStartOfWeek(now.minus(2, DateTimeUnit.WEEK))
        val toDate = DateTimeUtil.getEndOfWeek(now.plus(2, DateTimeUnit.WEEK))
        val lessons = serviceApi.getSchedule(
            groupId = group.id,
            fromDate = fromDate,
            toDate = toDate,
            userDataVersion = settingsRepository.getDataVersions().userDataVersion
        ).getOrElse {
            return false
        }

        scheduleDao.deleteTable()
        scheduleDao.insertAllClasses(lessons.map(NetworkLesson::toEntity))

        synchronizer.updateChangeListVersions {
            copy(scheduleDataVersion = remoteDataVersion.scheduleVersion)
        }
        return true
    }
}