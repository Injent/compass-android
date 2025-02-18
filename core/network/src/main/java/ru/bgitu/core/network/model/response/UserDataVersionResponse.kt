package ru.bgitu.core.network.model.response

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable
data class UserDataVersionResponse(
    @SerialName("userDataVersion") val userDataVersion: Int,
    @SerialName("scheduleUploadDate") @Serializable(LocalDataTimeSerializer::class) val scheduleUploadDate: LocalDateTime
)

private object LocalDataTimeSerializer : KSerializer<LocalDateTime> {
    override fun deserialize(decoder: Decoder): LocalDateTime {
        return LocalDateTime.parse(
            StringBuilder(decoder.decodeString()).apply {
                setCharAt(10, 'T')
            }.toString()
        )
    }

    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("LocalDateTime", PrimitiveKind.SHORT)

    override fun serialize(encoder: Encoder, value: LocalDateTime) = error("Not used")
}