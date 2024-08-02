package ru.bgitu.core.network.ktor

import io.ktor.client.plugins.api.createClientPlugin
import io.ktor.content.TextContent
import io.ktor.http.ContentType
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

private const val HEADER_X_SIGNATURE = "X-Signature"

val Crypto = createClientPlugin("CryptoSend") {
    transformRequestBody { request, content, _ ->
        if (!request.headers.contains(HEADER_X_SIGNATURE)) return@transformRequestBody null

        TextContent(
            text = Json.encodeToString(content),
            contentType = ContentType.Application.Json
        )
    }
}