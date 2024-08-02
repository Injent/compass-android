package ru.bgitu.core.datastore

import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import ru.bgitu.core.model.UserRole
import java.io.InputStream
import java.io.OutputStream

object SettingsSerializer : Serializer<SettingsPb> {
    override val defaultValue: SettingsPb = SettingsPb.getDefaultInstance()
        .copy {
            prefs = userPrefsPb {
                showGroupsOnMainScreen = true
            }
            userdata = userDataPb {
                role = UserRole.REGULAR.name
            }
        }

    override suspend fun readFrom(input: InputStream): SettingsPb {
        return try {
            SettingsPb.parseFrom(input)
        } catch (e: InvalidProtocolBufferException) {
            e.printStackTrace()
            SettingsPb.getDefaultInstance()
        } catch (e: Exception) {
            e.printStackTrace()
            SettingsPb.getDefaultInstance()
        }
    }

    override suspend fun writeTo(t: SettingsPb, output: OutputStream) = t.writeTo(output)
}