package ru.bgitu.core.network.model.response

import kotlinx.serialization.KSerializer
import kotlinx.serialization.PolymorphicSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.modules.SerializersModule

@Serializable(with = EosAuthTokenResponse.Serializer::class)
sealed interface EosAuthTokenResponse {
    @SerialName("state")
    val type: Int

    @Serializable
    data class Error(
        val msg: String
    ) : EosAuthTokenResponse {
        override val type = -1
    }

    @Serializable
    data class Success(
        val data: Data
    ) : EosAuthTokenResponse {
        override val type = 1

        @Serializable
        data class Data(
            val data: UserData,
            val accessToken: String
        ) {
            @Serializable
            data class UserData(
                val id: Int
            )
        }
    }

    object Serializer : KSerializer<EosAuthTokenResponse> {
        private val json = Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
            classDiscriminator = "state"
            serializersModule = SerializersModule {
                polymorphic(EosAuthTokenResponse::class, Success::class, Success.serializer())
                polymorphic(EosAuthTokenResponse::class, Error::class, Error.serializer())
            }
        }

        override val descriptor: SerialDescriptor
            get() = PolymorphicSerializer(EosAuthTokenResponse::class).descriptor

        override fun deserialize(decoder: Decoder): EosAuthTokenResponse {
            val jsonElement = (decoder as JsonDecoder).decodeJsonElement()
            return when (val state = jsonElement.jsonObject["state"]?.jsonPrimitive?.int) {
                -1 -> json.decodeFromJsonElement(Error.serializer(), jsonElement)
                1 -> json.decodeFromJsonElement(Success.serializer(), jsonElement)
                else -> throw SerializationException("Unknown state: $state")
            }
        }

        override fun serialize(encoder: Encoder, value: EosAuthTokenResponse) =
            error("Not need to serialize")
    }
}