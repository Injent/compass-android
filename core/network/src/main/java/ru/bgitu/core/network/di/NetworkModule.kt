package ru.bgitu.core.network.di

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.cio.CIO
import io.ktor.client.engine.cio.endpoint
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.cache.storage.FileStorage
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.URLProtocol
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import ru.bgitu.core.common.TextResource
import ru.bgitu.core.common.di.CommonQualifiers
import ru.bgitu.core.common.exception.DetailedException
import ru.bgitu.core.network.R

val NetworkModule = module {
    includes(FlavoredNetworkModule)

    single {
        HttpClient(CIO) {
            expectSuccess = true
            followRedirects = true

            engine {
                endpoint {
                    keepAliveTime = 5000
                    connectTimeout = 5000
                    connectAttempts = 2
                }
            }

            defaultRequest {
                url {
                    protocol = URLProtocol.HTTP
                }
                header(HttpHeaders.ContentType, "application/json")
            }

            configureResponseValidator()

            install(ContentNegotiation) {
                json(get())
            }

            install(HttpCache) {
                privateStorage(
                    FileStorage(
                        directory = androidContext().cacheDir,
                        dispatcher = get(CommonQualifiers.DispatcherIO)
                    )
                )
            }

            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        Log.d("Ktor", message)
                    }
                }
                level = LogLevel.ALL
            }
        }
    }
}

private fun HttpClientConfig<*>.configureResponseValidator() {
    HttpResponseValidator {
        validateResponse { response ->
            if (response.status.isSuccess()) return@validateResponse

            val errorResId = when (response.status) {
                HttpStatusCode.Unauthorized -> R.string.error_wrong_credentials
                HttpStatusCode.Forbidden -> R.string.error_wrong_credentials
                HttpStatusCode.NotFound -> R.string.error_unknown
                in HttpStatusCode.InternalServerError..HttpStatusCode.GatewayTimeout -> {
                    R.string.error_on_server
                }
                HttpStatusCode.RequestTimeout -> R.string.error_slow_internet_connection
                else -> {
                    R.string.error_unknown
                }
            }

            throw DetailedException(
                details = TextResource.Id(errorResId)
            )
        }
    }
}