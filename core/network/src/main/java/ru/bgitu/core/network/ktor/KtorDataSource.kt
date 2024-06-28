package ru.bgitu.core.network.ktor

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.engine.cio.endpoint
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.cache.storage.FileStorage
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.client.statement.bodyAsText
import io.ktor.client.statement.readBytes
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.URLProtocol
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.datetime.LocalDate
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.decodeFromJsonElement
import ru.bgitu.core.common.Result
import ru.bgitu.core.common.TextResource
import ru.bgitu.core.common.exception.DetailedException
import ru.bgitu.core.common.getOrNull
import ru.bgitu.core.common.runResulting
import ru.bgitu.core.datastore.SettingsRepository
import ru.bgitu.core.model.CompassAccount
import ru.bgitu.core.model.Group
import ru.bgitu.core.model.ProfessorClass
import ru.bgitu.core.model.RemoteDataVersions
import ru.bgitu.core.model.StatisticsModel
import ru.bgitu.core.model.settings.UserCredentials
import ru.bgitu.core.network.BuildConfig
import ru.bgitu.core.network.CompassService
import ru.bgitu.core.network.R
import ru.bgitu.core.network.model.NetworkLesson
import ru.bgitu.core.network.model.NetworkSearchMateItem
import ru.bgitu.core.network.model.NetworkSubject
import ru.bgitu.core.network.model.NetworkUserProfile
import ru.bgitu.core.network.model.request.ChooseGroupRequest
import ru.bgitu.core.network.model.request.HeadmanRequest
import ru.bgitu.core.network.model.request.RegisterCmtRequest
import ru.bgitu.core.network.model.request.RegisterEosUserRequest
import ru.bgitu.core.network.model.request.RegisterGuestRequest
import ru.bgitu.core.network.model.response.EosAuthResponse
import ru.bgitu.core.network.model.response.EosAuthTokenResponse
import ru.bgitu.core.network.model.response.RefreshTokenResponse
import ru.bgitu.core.network.model.response.RegisterEosUserResponse
import ru.bgitu.core.network.model.response.RegisterGuestAuthResponse
import ru.bgitu.core.network.model.response.UpdateAvailabilityResponse
import java.io.File

internal object EosRoutes {
    private const val BASE_URL = BuildConfig.EOS_URL
    const val AUTH_TOKEN = "$BASE_URL/api/tokenauth"
    const val STUDENT_INFO = "$BASE_URL/api/UserInfo/Student"
}

internal object CompassRoutes {
    private const val BASE_URL = BuildConfig.BACKEND_URL
    const val USER_ROUTE = "$BASE_URL/user"
    const val SEARCH_MATE = "$BASE_URL/mates"
    const val REGISTER_GUEST = "$BASE_URL/account/registerGuest"
    const val REGISTER_EOS_USER = "$BASE_URL/account/registerEosUser"
    const val CHOOSE_GROUP = "$BASE_URL/account/chooseGroup"
    const val ACCOUNT = "$BASE_URL/account"
    const val REFRESH_TOKEN = "$BASE_URL/refreshToken"
    const val LESSONS = "$BASE_URL/lessons"
    const val UPDATE_AVAILABILITY = "$BASE_URL/updateAvailability"
    const val GROUPS = "$BASE_URL/groups"
    const val SEARCH_TEACHER = "$BASE_URL/teacherSearch"
    const val SUBJECTS = "$BASE_URL/subjects"
    const val USER_AGREEMENT = "$BASE_URL/userAgreement"
    const val REGISTER_CMT = "$BASE_URL/account/registerCMT"
    const val SCHEDULE_VERSION = "$BASE_URL/scheduleVersion"
    const val CHANGELOG = "$BASE_URL/changelog"
}

class KtorDataSource(
    private val json: Json,
    private val settingsRepository: SettingsRepository,
    private val cacheDir: File,
    private val ioDispatcher: CoroutineDispatcher
) : CompassService {
    private var client = buildClient()

    fun rebuildClient() {
        client = buildClient()
    }

    override suspend fun searchMates(
        subjectName: String,
        variant: Int
    ) = runResulting {
        client.get {
            url(CompassRoutes.SEARCH_MATE)
            parameter("subject", subjectName)
            parameter("variant", variant)
        }.body<List<NetworkSearchMateItem>>()
    }

    override suspend fun getUserProfile(userId: Int) = runResulting {
        client.get {
            url( "${CompassRoutes.USER_ROUTE}/$userId")
        }.body<NetworkUserProfile>()
    }

    override suspend fun updateUserProfile(userProfile: NetworkUserProfile): Result<Unit> {
        return runResulting {
            client.post {
                url("${CompassRoutes.USER_ROUTE}/${userProfile.userId}")
                setBody(userProfile)
            }
        }
    }

    override suspend fun registerGuest(request: RegisterGuestRequest) = runResulting {
        client.post {
            url(CompassRoutes.REGISTER_GUEST)
            setBody(request)
        }.body<RegisterGuestAuthResponse>()
    }

    override suspend fun registerEosUser(request: RegisterEosUserRequest) = runResulting {
        client.post {
            url(CompassRoutes.REGISTER_EOS_USER)
            setBody(request)
        }.body<RegisterEosUserResponse>()
    }

    override suspend fun chooseGroup(group: Group) = runResulting<Unit> {
        client.put {
            url(CompassRoutes.CHOOSE_GROUP)
            setBody(ChooseGroupRequest(groupId = group.id, groupName = group.name))
        }
    }

    override suspend fun getAccount() = runResulting {
        client.get {
            url(CompassRoutes.ACCOUNT)
        }.body<CompassAccount>()
    }

    override suspend fun eosAuthToken(
        login: String,
        password: String
    ) = runResulting {
        val eosResponse = client.post {
            url(EosRoutes.AUTH_TOKEN)
        }.body<JsonObject>()

        val authTokenResponse = json.decodeFromJsonElement<EosAuthTokenResponse>(eosResponse)
        if (authTokenResponse is EosAuthTokenResponse.Error) {
            return Result.Failure(details = TextResource.Plain(authTokenResponse.msg))
        }

        authTokenResponse as EosAuthTokenResponse.Success
        val studentInfoBody = client.get {
            url(EosRoutes.STUDENT_INFO)
            header(HttpHeaders.Authorization, "Bearer ${authTokenResponse.data.accessToken}")
            parameter("studentID", authTokenResponse.data.data.id)
        }.body<JsonObject>()

        val data = EosAuthResponse(studentInfoBody)
        if (data.isStudent.not()) {
            throw DetailedException(details = TextResource.Id(R.string.error_not_student))
        }
        data
    }

    override suspend fun getScheduleVersion(groupId: Int) = runResulting {
        client.get {
            url(CompassRoutes.SCHEDULE_VERSION)
            header("DataVersion", 2)
            parameter("groupId", groupId)
        }.body<RemoteDataVersions>()
    }

    override suspend fun getSubjects(groupId: Int) = runResulting {
        client.get {
            url(CompassRoutes.SUBJECTS)
            parameter("groupId", groupId)
        }.body<List<NetworkSubject>>()
    }

    override suspend fun getSchedule(
        groupId: Int,
        fromDate: LocalDate,
        toDate: LocalDate
    ) = runResulting {
        client.get {
            url(CompassRoutes.LESSONS)
            parameter("groupId", groupId)
            parameter("startAt", fromDate)
            parameter("endAt", toDate)
        }.body<List<NetworkLesson>>()
    }

    override suspend fun registerCmt(
        request: RegisterCmtRequest
    ): Result<Unit> = runResulting {
        client.post {
            url(CompassRoutes.REGISTER_CMT)
            setBody(request)
        }
    }

    override suspend fun headmanRequest(request: HeadmanRequest) {
        //
    }

    override suspend fun refreshToken(refreshToken: String) = runResulting {
        client.get {
            url(CompassRoutes.REFRESH_TOKEN)
            parameter("refreshToken", refreshToken)
        }.body<RefreshTokenResponse>()
    }

    override suspend fun userAgreement() = runResulting {
        client.get {
            url(CompassRoutes.USER_AGREEMENT)
        }.bodyAsText()
    }

    override suspend fun searchGroups(query: String) = runResulting {
        client.get {
            url(CompassRoutes.GROUPS)
            parameter("searchQuery", query)
        }.body<List<Group>>()
    }

    override suspend fun getChangelog(versionCode: Long) = runResulting {
        client.get {
            url(CompassRoutes.CHANGELOG)
            parameter("version", versionCode)
        }.readBytes()
    }

    override suspend fun sendStatistics(
        statisticsModel: StatisticsModel
    ): Result<Unit> = runResulting {
       //
    }

    override suspend fun searchProfessors(query: String) = runResulting {
        client.get {
            url(CompassRoutes.SEARCH_TEACHER)
            parameter("searchQuery", query)
        }.body<List<String>>()
    }

    override suspend fun getProfessorSchedule(
        professorName: String,
        from: LocalDate,
        to: LocalDate
    ) = runResulting {
        client.get {
            url(CompassRoutes.SEARCH_TEACHER)
            parameter("teacher", professorName)
            parameter("dateFrom", from)
            parameter("dateTo", to)
        }.body<List<ProfessorClass>>()
    }

    override suspend fun getUpdateAvailability() = runResulting {
        client.get {
            url(CompassRoutes.UPDATE_AVAILABILITY)
        }.body<UpdateAvailabilityResponse>()
    }

    private fun buildClient(): HttpClient {
        return HttpClient(CIO) {
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
                json(json)
            }

            install(Auth) {
                bearer {
                    settingsRepository.provideCredentials()?.let { credentials ->
                        loadTokens {
                            BearerTokens(
                                accessToken = credentials.accessToken,
                                refreshToken = credentials.refreshToken
                            )
                        }

                        refreshTokens {
                            val response = refreshToken(refreshToken = credentials.refreshToken)
                                .getOrNull() ?: return@refreshTokens run {
                                BearerTokens(
                                    accessToken = credentials.accessToken,
                                    refreshToken = credentials.refreshToken
                                )
                            }

                            settingsRepository.setCredentials(
                                UserCredentials(
                                    accessToken = response.accessToken,
                                    refreshToken = response.refreshToken,
                                    expirationDate = response.expirationDate,
                                    authDate = response.requestDate
                                )
                            )
                            BearerTokens(
                                accessToken = response.accessToken,
                                refreshToken = response.refreshToken
                            )
                        }
                    }
                }
            }

            install(HttpCache) {
                privateStorage(FileStorage(cacheDir, ioDispatcher))
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