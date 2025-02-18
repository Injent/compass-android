package ru.bgitu.feature.schedule_widget.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.glance.appwidget.GlanceAppWidgetManager
import kotlinx.coroutines.runBlocking
import ru.bgitu.feature.schedule_widget.widget.ScheduleWidget

class BootCompletedReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Intent.ACTION_BOOT_COMPLETED) return

        val widgetIsNotExists = runBlocking {
            GlanceAppWidgetManager(context).getGlanceIds(ScheduleWidget::class.java)
        }.isEmpty()

        if (widgetIsNotExists) return

        context.sendBroadcast(Intent(context, ScheduleWidgetReceiver::class.java))
    }
}