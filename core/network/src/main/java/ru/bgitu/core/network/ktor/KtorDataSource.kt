package ru.bgitu.core.network.ktor

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.onDownload
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.client.statement.bodyAsText
import io.ktor.client.statement.readBytes
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate
import ru.bgitu.core.common.Result
import ru.bgitu.core.common.map
import ru.bgitu.core.common.runResulting
import ru.bgitu.core.datastore.SettingsRepository
import ru.bgitu.core.model.Group
import ru.bgitu.core.model.ProfessorClass
import ru.bgitu.core.model.RemoteDataVersions
import ru.bgitu.core.network.CompassService
import ru.bgitu.core.network.model.NetworkLesson
import ru.bgitu.core.network.model.NetworkSearchMateItem
import ru.bgitu.core.network.model.NetworkUserProfile
import ru.bgitu.core.network.model.request.ChooseGroupRequest
import ru.bgitu.core.network.model.request.ExternalAuthRequest
import ru.bgitu.core.network.model.request.RegisterCmtRequest
import ru.bgitu.core.network.model.response.CredentialsResponse
import ru.bgitu.core.network.model.response.RefreshTokenResponse
import ru.bgitu.core.network.model.response.UpdateAvailabilityResponse
import ru.bgitu.core.network.model.response.UserDataVersionResponse

internal object CompassRoutes {
    private const val BASE_URL = "http://api.bgitu-compass.ru"
    const val USER_ROUTE = "$BASE_URL/user"
    const val SEARCH_MATE = "$BASE_URL/mates"
    const val CHOOSE_GROUP = "$BASE_URL/account/chooseGroup"
    const val REFRESH_TOKEN = "$BASE_URL/refreshToken"
    const val LESSONS = "$BASE_URL/lessons"
    const val UPDATE_AVAILABILITY = "$BASE_URL/updateAvailability"
    const val GROUPS = "$BASE_URL/groups"
    const val SEARCH_TEACHER = "$BASE_URL/teacherSearch"
    const val USER_AGREEMENT = "$BASE_URL/userAgreement"
    const val REGISTER_CMT = "$BASE_URL/account/registerCMT"
    const val SCHEDULE_VERSION = "$BASE_URL/scheduleVersion"
    const val CHANGELOG = "$BASE_URL/changelog"
    const val EXTERNAL_AUTH = "$BASE_URL/auth"
    const val SCHEDULE_UPDATE_DATE = "$BASE_URL/scheduleUpdateDate"
}

class KtorDataSource(
    private val client: HttpClient,
    settingsRepository: SettingsRepository,
) : CompassService {

    init {
        client.authInterceptor(settingsRepository)
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

    override suspend fun getUserProfile(userId: Long) = runResulting {
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

    override suspend fun chooseGroup(group: Group) = runResulting<Unit> {
        client.put {
            url(CompassRoutes.CHOOSE_GROUP)
            setBody(ChooseGroupRequest(groupId = group.id, groupName = group.name))
        }
    }

    override suspend fun getUserDataVersion(): Result<Int> = runResulting {
        client.get {
            url(CompassRoutes.SCHEDULE_UPDATE_DATE)
        }.body<UserDataVersionResponse>()
    }.map { it.userDataVersion }

    override suspend fun getScheduleVersion(groupId: Int) = runResulting {
        client.get {
            url(CompassRoutes.SCHEDULE_VERSION)
            header("DataVersion", 2)
            parameter("groupId", groupId)
        }.body<RemoteDataVersions>()
    }

    override suspend fun getSchedule(
        groupId: Int,
        fromDate: LocalDate,
        toDate: LocalDate,
        userDataVersion: Int
    ): Result<List<NetworkLesson>> = runResulting {
        client.get {
            url(CompassRoutes.LESSONS)
            parameter("groupId", groupId)
            parameter("startAt", fromDate)
            parameter("endAt", toDate)
            header("UserData-Version", userDataVersion)
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

    override suspend fun authWithExternalService(request: ExternalAuthRequest) = runResulting {
        client.post {
            url(CompassRoutes.EXTERNAL_AUTH)
            setBody(request)
        }.body<CredentialsResponse>()
    }
}

