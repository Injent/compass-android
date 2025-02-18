package ru.bgitu.feature.schedule_widget.receiver

import android.annotation.SuppressLint
import android.appwidget.AppWidgetManager
import android.content.Context
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import ru.bgitu.core.data.util.SyncManager
import ru.bgitu.feature.schedule_widget.widget.ScheduleWidget

class ScheduleWidgetReceiver : GlanceAppWidgetReceiver(), KoinComponent {
    override val glanceAppWidget = ScheduleWidget()

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        get<SyncManager>().requestSync()
    }
}