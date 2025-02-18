package ru.bgitu.core.network.ktor

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.client.statement.readRawBytes
import kotlinx.datetime.LocalDate
import ru.bgitu.core.common.Result
import ru.bgitu.core.common.runResulting
import ru.bgitu.core.datastore.SettingsRepository
import ru.bgitu.core.model.Group
import ru.bgitu.core.model.ProfessorClass
import ru.bgitu.core.model.RemoteDataVersions
import ru.bgitu.core.network.CompassService
import ru.bgitu.core.network.ktor.CompassRoutes.LESSONS_V2
import ru.bgitu.core.network.model.request.ChooseGroupRequest
import ru.bgitu.core.network.model.response.ScheduleResponse
import ru.bgitu.core.network.model.response.UpdateAvailabilityResponse
import ru.bgitu.core.network.model.response.UserDataVersionResponse

internal object CompassRoutes {
    private const val BASE_URL = "http://api.bgitu-compass.ru"
    const val CHOOSE_GROUP = "$BASE_URL/account/chooseGroup"
    const val REFRESH_TOKEN = "$BASE_URL/refreshToken"
    const val UPDATE_AVAILABILITY = "$BASE_URL/updateAvailability"
    const val GROUPS = "$BASE_URL/groups"
    const val SEARCH_TEACHER = "$BASE_URL/v2/teacherSearch"
    const val SCHEDULE_VERSION = "$BASE_URL/scheduleVersion"
    const val CHANGELOG = "$BASE_URL/changelog"
    const val SCHEDULE_UPDATE_DATE = "$BASE_URL/scheduleUpdateDate"
    const val LESSONS_V2 = "$BASE_URL/v2/lessons"
}

class KtorDataSource(
    private val client: HttpClient,
    settingsRepository: SettingsRepository,
) : CompassService {

    init {
        client.authInterceptor(settingsRepository)
    }

    override suspend fun chooseGroup(group: Group) = runResulting<Unit> {
        client.put {
            url(CompassRoutes.CHOOSE_GROUP)
            setBody(ChooseGroupRequest(groupId = group.id, groupName = group.name))
        }
    }

    override suspend fun getUserDataVersion(): Result<UserDataVersionResponse> = runResulting {
        client.get {
            url(CompassRoutes.SCHEDULE_UPDATE_DATE)
        }.body<UserDataVersionResponse>()
    }

    override suspend fun getScheduleVersion(groupId: Int) = runResulting {
        client.get {
            url(CompassRoutes.SCHEDULE_VERSION)
            header("DataVersion", 2)
            parameter("groupId", groupId)
        }.body<RemoteDataVersions>()
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
        }.readRawBytes()
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

    override suspend fun getFullSchedule(groupId: Int, cached: Boolean): Result<ScheduleResponse> = runResulting {
        client.get {
            url(LESSONS_V2)
            parameter("groupId", groupId)
            parameter("cache", cached)
        }.body<ScheduleResponse>()
    }
}

