package ru.bgitu.feature.schedule_widget.widget

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.glance.state.GlanceStateDefinition
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import ru.bgitu.core.SettingsPb
import java.io.File

object ScheduleStateDefinition : GlanceStateDefinition<SettingsPb>, KoinComponent {

    override suspend fun getDataStore(
        context: Context,
        fileKey: String
    ): DataStore<SettingsPb> = get()

    override fun getLocation(context: Context, fileKey: String): File {
        return File(context.cacheDir, "no_op")
    }
}