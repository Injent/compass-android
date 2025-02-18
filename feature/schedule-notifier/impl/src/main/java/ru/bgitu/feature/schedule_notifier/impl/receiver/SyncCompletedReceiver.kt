package ru.bgitu.feature.schedule_notifier.impl.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.bgitu.core.data.util.SyncManager
import ru.bgitu.feature.schedule_notifier.api.ScheduleNotifier

class SyncCompletedReceiver : BroadcastReceiver(), KoinComponent {
    private val scheduleNotifier by inject<ScheduleNotifier>()

    override fun onReceive(context: Context, intent: Intent) {
        val result = intent.getIntExtra(SyncManager.EXTRA_RESULT, SyncManager.SYNC_FAIL)
        val manualSync = intent.getBooleanExtra(SyncManager.EXTRA_IS_MANUAL, false)

        if (result == SyncManager.SYNC_FAIL || !manualSync) return

        scheduleNotifier.restart(forced = true)
    }
}