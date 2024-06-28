package ru.bgitu.feature.schedule_widget.work

import android.content.Context
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.appwidget.updateAll
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.ForegroundInfo
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDate
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.component.inject
import ru.bgitu.core.common.DEFAULT_WORK_RETRY_ATTEMPTS
import ru.bgitu.core.data.repository.ScheduleRepository
import ru.bgitu.core.model.Lesson
import ru.bgitu.core.notifications.Notifier
import ru.bgitu.feature.schedule_widget.WidgetPb.WidgetDataPb
import ru.bgitu.feature.schedule_widget.copy
import ru.bgitu.feature.schedule_widget.model.toWidgetLesson
import ru.bgitu.feature.schedule_widget.widget.ScheduleStateDefinition
import ru.bgitu.feature.schedule_widget.widget.ScheduleWidget

class ScheduleWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params), KoinComponent {
    private val scheduleRepository by inject<ScheduleRepository>()

    override suspend fun getForegroundInfo(): ForegroundInfo {
        val notifier = get<Notifier>()
        return ForegroundInfo(
            Notifier.BACKGROUND_NOTIFICATION_ID,
            notifier.backgroundWorkNotification()
        )
    }

    override suspend fun doWork(): Result {
        return runCatching {
            val queryDate = inputData.getString(QUERY_DATE)?.toLocalDate()
                ?: throw IllegalArgumentException("QUERY_DATE not presented")

            updateWidgetState {
                it.copy {
                    isLoading = true
                    this.queryDate = queryDate.toString()
                }
            }
            ScheduleWidget().updateAll(applicationContext)

            val from = queryDate.minus(1, DateTimeUnit.DAY)
            val to = queryDate.plus(1, DateTimeUnit.DAY)

            val lessons = scheduleRepository.getClasses(from, to)
            updateWidgetState {
                it.copy {
                    this.isLoading = false
                    this.lessons.clear()
                    this.lessons.addAll(lessons.map(Lesson::toWidgetLesson))
                }
            }
            ScheduleWidget().updateAll(applicationContext)
            Result.success()
        }.getOrElse { e ->
            e.printStackTrace()
            if (runAttemptCount <= DEFAULT_WORK_RETRY_ATTEMPTS) {
                Result.retry()
            } else Result.failure()
        }
    }

    private suspend fun updateWidgetState(updateState: (WidgetDataPb) -> WidgetDataPb) {
        GlanceAppWidgetManager(applicationContext).getGlanceIds(ScheduleWidget::class.java)
            .forEach { id ->
                updateAppWidgetState(
                    context = applicationContext,
                    definition = ScheduleStateDefinition,
                    glanceId = id,
                    updateState = updateState
                )
            }
    }

    companion object {
        private val scheduleWorkName = ScheduleWorker::class.simpleName!!
        private const val SCHEDULE_WORK_TAG = "widget_schedule_work"
        private const val QUERY_DATE = "query_date"

        fun start(context: Context, queryDate: LocalDate) {
            val request = OneTimeWorkRequestBuilder<ScheduleWorker>()
                .addTag(SCHEDULE_WORK_TAG)
                .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
                .setInputData(
                    workDataOf(
                        QUERY_DATE to queryDate.toString()
                    )
                )
                .build()

            val manager = WorkManager.getInstance(context)
            manager.enqueueUniqueWork(
                scheduleWorkName,
                ExistingWorkPolicy.REPLACE,
                request
            )
        }

        fun cancel(context: Context) {
            WorkManager.getInstance(context).cancelAllWorkByTag(SCHEDULE_WORK_TAG)
        }
    }
}