package ru.bgitu.core.network

import kotlinx.datetime.LocalDate
import ru.bgitu.core.common.Result
import ru.bgitu.core.model.Group
import ru.bgitu.core.model.ProfessorClass
import ru.bgitu.core.model.RemoteDataVersions
import ru.bgitu.core.network.model.response.ScheduleResponse
import ru.bgitu.core.network.model.response.UpdateAvailabilityResponse
import ru.bgitu.core.network.model.response.UserDataVersionResponse

interface CompassService {
    suspend fun chooseGroup(group: Group): Result<Unit>
    suspend fun getUserDataVersion(): Result<UserDataVersionResponse>
    suspend fun getScheduleVersion(groupId: Int): Result<RemoteDataVersions>
    suspend fun searchGroups(query: String): Result<List<Group>>
    suspend fun getChangelog(versionCode: Long): Result<ByteArray>
    suspend fun searchProfessors(query: String): Result<List<String>>
    suspend fun getProfessorSchedule(
        professorName: String,
        from: LocalDate,
        to: LocalDate
    ): Result<List<ProfessorClass>>
    suspend fun getUpdateAvailability(): Result<UpdateAvailabilityResponse>
    suspend fun getFullSchedule(groupId: Int, cached: Boolean = false): Result<ScheduleResponse>
}