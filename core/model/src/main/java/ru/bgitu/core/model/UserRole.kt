package ru.bgitu.core.model

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(UserRoleSerializer::class)
enum class UserRole(val nameId: Int) {
    STUDENT(R.string.role_student),
    HEADMAN(R.string.role_headman),
    DEPUTY_HEADMAN(R.string.role_deputy_headman)
}

class UserRoleSerializer : KSerializer<UserRole> {

    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("user_role", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): UserRole {
        return UserRole.valueOf(decoder.decodeString().uppercase())
    }

    override fun serialize(encoder: Encoder, value: UserRole) {
        encoder.encodeString(value.toString().lowercase())
    }
}