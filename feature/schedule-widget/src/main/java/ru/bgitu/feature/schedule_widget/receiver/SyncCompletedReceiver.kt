package ru.bgitu.feature.schedule_widget.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.glance.appwidget.GlanceAppWidgetManager
import kotlinx.coroutines.runBlocking
import ru.bgitu.core.common.DateTimeUtil
import ru.bgitu.core.copy
import ru.bgitu.core.data.util.SyncManager
import ru.bgitu.core.data.util.SyncManager.Companion.EXTRA_RESULT
import ru.bgitu.feature.schedule_widget.updateScheduleWidgetState
import ru.bgitu.feature.schedule_widget.widget.ScheduleWidget

class SyncCompletedReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) = runBlocking {
        val result = intent.getIntExtra(EXTRA_RESULT, -1)

        if (result == SyncManager.SYNC_FAIL) return@runBlocking

        val widgetManager = GlanceAppWidgetManager(context)
        widgetManager.getGlanceIds(ScheduleWidget::class.java).forEach { glanceId ->
            context.updateScheduleWidgetState(
                glanceId = glanceId,
                updateState = {
                    it.copy {
                        queryDate = DateTimeUtil.currentDate.toString()
                        isLoading = false
                        error = false
                    }
                }
            )
        }
    }
}