package ru.bgitu.feature.schedule_widget.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.glance.appwidget.GlanceAppWidgetManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.atDate
import kotlinx.datetime.plus
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import ru.bgitu.core.common.DateTimeUtil
import ru.bgitu.core.common.di.AppDispatchers
import ru.bgitu.core.copy
import ru.bgitu.core.data.repository.ScheduleRepository
import ru.bgitu.core.data.util.SyncManager
import ru.bgitu.core.data.util.SyncManager.Companion.EXTRA_RESULT
import ru.bgitu.feature.schedule_widget.updateScheduleWidgetState
import ru.bgitu.feature.schedule_widget.widget.ScheduleWidget

class SyncCompletedReceiver : BroadcastReceiver(), KoinComponent {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.getIntExtra(EXTRA_RESULT, -1) == SyncManager.SYNC_FAIL) {
            return
        }
        val scheduleRepository = get<ScheduleRepository>()

        CoroutineScope(get<AppDispatchers>().mainDispatcher).launch {
            val widgetDate = getWidgetDate(
                scheduleRepository = scheduleRepository,
                now = DateTimeUtil.currentDateTime
            )

            val widgetManager = GlanceAppWidgetManager(context)
            widgetManager.getGlanceIds(ScheduleWidget::class.java)
                .forEach { glanceId ->
                    context.updateScheduleWidgetState(
                        glanceId = glanceId,
                        updateState = {
                            it.copy {
                                queryDate = widgetDate.toString()
                                isLoading = false
                                error = false
                            }
                        }
                    )
                }
        }
    }

    private suspend fun getWidgetDate(
        scheduleRepository: ScheduleRepository,
        now: LocalDateTime
    ): LocalDate {
        val lessonEndTime = scheduleRepository.getLessonsForDate(now.date)
            .maxByOrNull { it.endAt }.let { it ?: return now.date }
            .endAt
            .atDate(now.date)

        return if (lessonEndTime < now) {
            now.date.plus(1, DateTimeUnit.DAY)
        } else {
            now.date
        }
    }
}