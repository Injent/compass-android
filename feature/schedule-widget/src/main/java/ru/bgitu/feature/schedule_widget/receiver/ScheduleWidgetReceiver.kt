package ru.bgitu.feature.schedule_widget.receiver

import android.appwidget.AppWidgetManager
import android.content.Context
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import ru.bgitu.core.common.DateTimeUtil
import ru.bgitu.feature.schedule_widget.widget.ScheduleWidget
import ru.bgitu.feature.schedule_widget.work.WidgetScheduleWorker

class ScheduleWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget = ScheduleWidget()

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        WidgetScheduleWorker.start(context, DateTimeUtil.currentDate)
    }

    override fun onEnabled(context: Context?) {
        super.onEnabled(context)
        if (context != null) {
            WidgetScheduleWorker.start(context, DateTimeUtil.currentDate)
        }
    }
}