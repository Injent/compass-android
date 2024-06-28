package ru.bgitu.feature.schedule_widget.widget

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.dataStoreFile
import androidx.glance.state.GlanceStateDefinition
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import ru.bgitu.feature.schedule_widget.WidgetPb.WidgetDataPb
import ru.bgitu.feature.schedule_widget.datastore.PrefsToProtoMigration
import ru.bgitu.feature.schedule_widget.datastore.WidgetDataSerializer
import ru.bgitu.feature.schedule_widget.model.ScheduleWidgetState
import java.io.File

object ScheduleStateDefinition : GlanceStateDefinition<WidgetDataPb> {
    private const val OLD_PREFS_STORE_FILE = "schedule.json"
    private const val DATASTORE_FILE = "schedule.pb"
    private var dataStore: DataStore<WidgetDataPb>? = null

    override suspend fun getDataStore(
        context: Context,
        fileKey: String
    ): DataStore<WidgetDataPb> {
        if (dataStore == null) {
            dataStore = context.createWidgetDataStore()
        }
        return dataStore ?: throw RuntimeException("Widget data store failed to create")
    }

    fun deleteDataStore(
        context: Context
    ) {
        context.dataStoreFile(DATASTORE_FILE).delete()
    }

    override fun getLocation(context: Context, fileKey: String): File {
        return context.dataStoreFile(DATASTORE_FILE)
    }

    private fun Context.createWidgetDataStore(): DataStore<WidgetDataPb> {
        return DataStoreFactory.create(
            serializer = WidgetDataSerializer,
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
            corruptionHandler = ReplaceFileCorruptionHandler {
                ScheduleWidgetState.defaultProtoInstance()
            },
            migrations = listOf(
                PrefsToProtoMigration(oldPrefsFile = dataStoreFile(OLD_PREFS_STORE_FILE))
            )
        ) {
            dataStoreFile(DATASTORE_FILE)
        }
    }
}