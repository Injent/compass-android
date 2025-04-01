package ru.bgitu.feature.schedule_widget.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.glance.appwidget.GlanceAppWidgetManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import ru.bgitu.core.common.di.AppDispatchers
import ru.bgitu.feature.schedule_widget.widget.ScheduleWidget

class BootCompletedReceiver : BroadcastReceiver(), KoinComponent {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Intent.ACTION_BOOT_COMPLETED) return

        CoroutineScope(get<AppDispatchers>().mainDispatcher).launch {
            val widgetIsNotExists = GlanceAppWidgetManager(context)
                .getGlanceIds(ScheduleWidget::class.java)
                .isEmpty()

            if (widgetIsNotExists) return@launch

            context.sendBroadcast(Intent(context, ScheduleWidgetReceiver::class.java))
        }
    }
}