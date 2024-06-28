package ru.bgitu.core.network.model.response

import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.long
import ru.bgitu.core.common.STUDENT_MARKER

class EosAuthResponse(
    rawJson: JsonObject,
) {
    private val data: JsonObject = rawJson["data"]!!.jsonObject

    val eosUserId: Long
        get() = data["studentID"]!!.jsonPrimitive.long

    val fullName: String
        get() = data["fullName"]!!.jsonPrimitive.content

    val isStudent: Boolean = rawJson["msg"]!!.jsonPrimitive.content == STUDENT_MARKER

    val groupName: String = data["group"]!!.jsonObject["item1"]!!.jsonPrimitive.content

    val avatarUrl: String
        get() = data["photoLink"]!!
            .jsonPrimitive.content
}
