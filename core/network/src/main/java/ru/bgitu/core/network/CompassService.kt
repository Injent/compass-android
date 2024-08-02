package ru.bgitu.core.network

import kotlinx.datetime.LocalDate
import ru.bgitu.core.common.Result
import ru.bgitu.core.model.Group
import ru.bgitu.core.model.ProfessorClass
import ru.bgitu.core.model.RemoteDataVersions
import ru.bgitu.core.network.model.NetworkLesson
import ru.bgitu.core.network.model.NetworkSearchMateItem
import ru.bgitu.core.network.model.NetworkSubject
import ru.bgitu.core.network.model.NetworkUserProfile
import ru.bgitu.core.network.model.request.ExternalAuthRequest
import ru.bgitu.core.network.model.request.RegisterCmtRequest
import ru.bgitu.core.network.model.response.CredentialsResponse
import ru.bgitu.core.network.model.response.RefreshTokenResponse
import ru.bgitu.core.network.model.response.UpdateAvailabilityResponse

interface CompassService {

    suspend fun getUserProfile(userId: Long): Result<NetworkUserProfile>

    suspend fun updateUserProfile(userProfile: NetworkUserProfile): Result<Unit>

    suspend fun searchMates(subjectName: String, variant: Int): Result<List<NetworkSearchMateItem>>

    suspend fun chooseGroup(group: Group): Result<Unit>

    suspend fun getScheduleVersion(groupId: Int): Result<RemoteDataVersions>
    suspend fun getSubjects(groupId: Int): Result<List<NetworkSubject>>
    suspend fun getSchedule(
        groupId: Int,
        fromDate: LocalDate,
        toDate: LocalDate
    ): Result<List<NetworkLesson>>
    suspend fun registerCmt(request: RegisterCmtRequest): Result<Unit>
    suspend fun refreshToken(refreshToken: String): Result<RefreshTokenResponse>
    suspend fun userAgreement(): Result<String>
    suspend fun searchGroups(query: String): Result<List<Group>>
    suspend fun getChangelog(versionCode: Long): Result<ByteArray>
    suspend fun searchProfessors(query: String): Result<List<String>>
    suspend fun getProfessorSchedule(
        professorName: String,
        from: LocalDate,
        to: LocalDate
    ): Result<List<ProfessorClass>>

    suspend fun getUpdateAvailability(): Result<UpdateAvailabilityResponse>
    suspend fun authWithExternalService(request: ExternalAuthRequest): Result<CredentialsResponse>
}