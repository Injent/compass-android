package ru.bgitu.components.signin.vk

import com.vk.id.AccessToken
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.forms.formData
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.encodedPath
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import ru.bgitu.components.signin.AuthClient
import ru.bgitu.components.signin.model.AuthMethod
import ru.bgitu.components.signin.model.SignInParams
import ru.bgitu.core.common.Result
import ru.bgitu.core.common.getOrElse
import ru.bgitu.core.common.runResulting

private const val VK_API_BASE_URL = "https://api.vk.com/method"

class VkAuthClient(
    private val accessToken: AccessToken,
    private val coroutineScope: CoroutineScope,
    private val onResult: (Result<SignInParams>) -> Unit,
) : AuthClient {

    override fun signIn() {
        coroutineScope.launch {
            val lastName = getLastName(accessToken.token)
                .getOrElse { return@launch }

            onResult(
                Result.Success(
                    createSignInParams(
                        accessToken = accessToken,
                        lastName = lastName
                    )
                )
            )
        }
    }

    private fun createSignInParams(accessToken: AccessToken, lastName: String) = SignInParams(
        authMethod = AuthMethod.VK,
        idToken = checkNotNull(accessToken.idToken) { "idToken can't be null" },
        fullName = "${accessToken.userData.firstName} $lastName",
        avatarUrl = accessToken.userData.photo200
    )

    private suspend fun getLastName(accessToken: String): Result<String> = runResulting {
        HttpClient(CIO).use { client ->
            client.post {
                url {
                    encodedPath = "$VK_API_BASE_URL/account.getProfileInfo"
                    contentType(ContentType.Application.FormUrlEncoded)
                }
                setBody(
                    formData {
                        append("access_token", accessToken)
                        append("v", "5.199")
                    }
                )
            }.body<JsonObject>()["response"]!!.jsonObject["last_name"]!!.jsonPrimitive.content
        }
    }
}