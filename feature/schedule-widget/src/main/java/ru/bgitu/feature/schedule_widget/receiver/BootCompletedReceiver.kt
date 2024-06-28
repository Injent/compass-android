package ru.bgitu.feature.schedule_widget.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class BootCompletedReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Intent.ACTION_BOOT_COMPLETED) return

        context.sendBroadcast(Intent(context, ScheduleWidgetReceiver::class.java))
    }
}