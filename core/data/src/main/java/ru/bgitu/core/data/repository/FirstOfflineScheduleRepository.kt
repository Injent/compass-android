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
import ru.bgitu.core.common.DateTimeUtil
import ru.bgitu.core.common.getOrElse
import ru.bgitu.core.common.getOrNull
import ru.bgitu.core.data.model.ScheduleLoadState
import ru.bgitu.core.data.model.asEntity
import ru.bgitu.core.data.model.toExternalModel
import ru.bgitu.core.data.util.Synchronizer
import ru.bgitu.core.database.dao.ScheduleDao
import ru.bgitu.core.database.dao.SubjectDao
import ru.bgitu.core.database.model.PopulatedLesson
import ru.bgitu.core.datastore.SettingsRepository
import ru.bgitu.core.model.Lesson
import ru.bgitu.core.network.CompassService
import ru.bgitu.core.network.model.NetworkLesson
import ru.bgitu.core.network.model.NetworkSubject

class FirstOfflineScheduleRepository(
    private val serviceApi: CompassService,
    private val settings: SettingsRepository,
    private val scheduleDao: ScheduleDao,
    private val subjectDao: SubjectDao
) : ScheduleRepository {
    override fun getClassesStream(currentDate: LocalDate, preloadWeekCount: Int): Flow<ScheduleLoadState> {
        return scheduleDao.getLessonsStream(
            DateTimeUtil.getStartOfWeek(currentDate).minus(preloadWeekCount, DateTimeUnit.WEEK),
            DateTimeUtil.getEndOfWeek(currentDate).plus(preloadWeekCount, DateTimeUnit.WEEK)
        )
            .map<List<PopulatedLesson>, ScheduleLoadState> {
                if (it.isEmpty()) return@map ScheduleLoadState.Success(emptyMap())

                val lessons = it.map(PopulatedLesson::toExternalModel)
                ScheduleLoadState.Success(
                    data = lessons.groupBy { lesson -> lesson.date }.toMap()
                )
            }
            .onEmpty {
                emit(ScheduleLoadState.Success(emptyMap()))
            }
            .catch {
                it.printStackTrace()
                emit(ScheduleLoadState.Error(it))
            }
    }

    override suspend fun getNetworkClasses(
        groupId: Int,
        from: LocalDate,
        to: LocalDate
    ): ScheduleLoadState {
        val lessons = serviceApi.getSchedule(
            groupId = groupId,
            fromDate = from,
            toDate = to
        )
            .getOrElse { return ScheduleLoadState.Error(it.throwable) }

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
        return scheduleDao.getClasses(queryDate).map(PopulatedLesson::toExternalModel)
    }

    override suspend fun getClasses(from: LocalDate, to: LocalDate, limit: Int): List<Lesson> {
        return scheduleDao.getClasses(from, to, limit).map(PopulatedLesson::toExternalModel)
    }

    override suspend fun syncWith(synchronizer: Synchronizer): Boolean {
        val groupId = settings.data.first().groupId ?: return false
        val dataVersion = synchronizer.dataVersions()

        val remoteDataVersion = serviceApi.getScheduleVersion(groupId).getOrElse { return false }

        if (dataVersion.scheduleDataVersion == remoteDataVersion.scheduleVersion) {
            return true
        }

        val now = DateTimeUtil.currentDate
        val fromDate = DateTimeUtil.getStartOfWeek(now.minus(2, DateTimeUnit.WEEK))
        val toDate = DateTimeUtil.getEndOfWeek(now.plus(2, DateTimeUnit.WEEK))
        val classes = serviceApi.getSchedule(
            groupId,
            fromDate,
            toDate
        )
            .getOrNull()
            ?.map(NetworkLesson::asEntity)
            ?: return false

        val subjects = serviceApi.getSubjects(groupId)
            .getOrNull()
            ?.map(NetworkSubject::asEntity)
            ?: return false

        subjectDao.deleteTable()
        subjectDao.insertAll(subjects)

        scheduleDao.deleteTable()
        scheduleDao.insertAllClasses(classes)

        synchronizer.updateChangeListVersions {
            copy(
                scheduleDataVersion = remoteDataVersion.scheduleVersion,
            )
        }
        return true
    }
}