package ru.bgitu.app.startup

import android.content.Context
import androidx.startup.Initializer
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import ru.bgitu.components.updates.api.AppUpdateManager
import ru.bgitu.core.data.util.SyncManager
import ru.bgitu.core.notifications.channels.AppChannelManager
import ru.bgitu.feature.schedule_notifier.api.ScheduleNotifier

class CommonInitializer : Initializer<Unit>, KoinComponent {
    override fun create(context: Context) {
        get<AppChannelManager>().ensureIfExists()
        get<AppUpdateManager>().safelyClearUpdates()
        get<SyncManager>().fullSync()
        get<ScheduleNotifier>().restart()
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return mutableListOf(KoinInitializer::class.java)
    }
}
