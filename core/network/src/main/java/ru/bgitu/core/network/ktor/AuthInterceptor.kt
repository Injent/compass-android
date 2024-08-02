package ru.bgitu.core.network.ktor

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpSend
import io.ktor.client.plugins.plugin
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.url
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.flow.first
import ru.bgitu.core.common.getOrElse
import ru.bgitu.core.common.runResulting
import ru.bgitu.core.datastore.SettingsRepository
import ru.bgitu.core.model.settings.UserCredentials
import ru.bgitu.core.network.model.response.RefreshTokenResponse

fun HttpClient.authInterceptor(settingsRepository: SettingsRepository) {
    plugin(HttpSend).intercept { request ->
        val credentials = settingsRepository.credentials.first()

        if (credentials != null) {
            request.bearerAuth(credentials.accessToken)
        }

        val originalCall = execute(request)

        if (originalCall.response.status == HttpStatusCode.Unauthorized && credentials != null) {
            val newCredentials = runResulting {
                get {
                    url(CompassRoutes.REFRESH_TOKEN)
                    parameter("refreshToken", credentials.refreshToken)
                }.body<RefreshTokenResponse>()
            }.getOrElse { return@intercept originalCall }

            settingsRepository.setCredentials(
                UserCredentials(
                    accessToken = newCredentials.accessToken,
                    refreshToken = newCredentials.refreshToken
                )
            )
            request.bearerAuth(newCredentials.accessToken)
            execute(request)
        } else {
            originalCall
        }
    }
}