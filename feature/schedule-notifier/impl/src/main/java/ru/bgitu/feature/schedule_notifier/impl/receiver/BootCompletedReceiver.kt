package ru.bgitu.feature.schedule_notifier.impl.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.bgitu.core.datastore.SettingsRepository

class BootCompletedReceiver : BroadcastReceiver(), KoinComponent {
    private val settings by inject<SettingsRepository>()

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Intent.ACTION_BOOT_COMPLETED) return

        val showPinnedSchedule = runBlocking { settings.data.first() }.userPrefs.showPinnedSchedule
        if (!showPinnedSchedule) return

        context.sendBroadcast(Intent(context, AlarmReceiver::class.java))
    }
}