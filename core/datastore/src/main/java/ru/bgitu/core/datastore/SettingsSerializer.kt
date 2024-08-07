package ru.bgitu.core.datastore

import androidx.datastore.core.Serializer
import ru.bgitu.core.SettingsPb
import java.io.InputStream
import java.io.OutputStream

object SettingsSerializer : Serializer<SettingsPb> {
    override val defaultValue: SettingsPb = SettingsPb.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): SettingsPb {
        return SettingsPb.parseFrom(input)
    }

    override suspend fun writeTo(t: SettingsPb, output: OutputStream) = t.writeTo(output)
}