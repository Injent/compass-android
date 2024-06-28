package ru.bgitu.feature.schedule_widget.datastore

import androidx.datastore.core.Serializer
import ru.bgitu.feature.schedule_widget.WidgetPb.WidgetDataPb
import ru.bgitu.feature.schedule_widget.model.ScheduleWidgetState
import java.io.InputStream
import java.io.OutputStream

object WidgetDataSerializer : Serializer<WidgetDataPb> {
    override val defaultValue: WidgetDataPb = ScheduleWidgetState.defaultProtoInstance()

    override suspend fun readFrom(input: InputStream): WidgetDataPb {
        return runCatching {
            WidgetDataPb.parseFrom(input)
        }
            .getOrDefault(defaultValue)
    }

    override suspend fun writeTo(t: WidgetDataPb, output: OutputStream) = t.writeTo(output)
}