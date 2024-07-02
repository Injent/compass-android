package ru.bgitu.core.data.testdoubles

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlinx.serialization.json.putJsonObject
import ru.bgitu.core.common.Result
import ru.bgitu.core.model.CompassAccount
import ru.bgitu.core.model.Group
import ru.bgitu.core.model.ProfessorClass
import ru.bgitu.core.model.RemoteDataVersions
import ru.bgitu.core.model.UserRole
import ru.bgitu.core.model.settings.UserCredentials
import ru.bgitu.core.network.CompassService
import ru.bgitu.core.network.model.NetworkLesson
import ru.bgitu.core.network.model.NetworkSearchMateItem
import ru.bgitu.core.network.model.NetworkSubject
import ru.bgitu.core.network.model.NetworkUserProfile
import ru.bgitu.core.network.model.request.RegisterCmtRequest
import ru.bgitu.core.network.model.request.RegisterEosUserRequest
import ru.bgitu.core.network.model.request.RegisterGuestRequest
import ru.bgitu.core.network.model.response.EosAuthResponse
import ru.bgitu.core.network.model.response.RefreshTokenResponse
import ru.bgitu.core.network.model.response.RegisterEosUserResponse
import ru.bgitu.core.network.model.response.RegisterGuestAuthResponse
import ru.bgitu.core.network.model.response.UpdateAvailabilityResponse
import kotlin.time.Duration.Companion.days

class TestCompassService : CompassService {

    override suspend fun registerGuest(request: RegisterGuestRequest) = Result.Success(
        value = RegisterGuestAuthResponse(
            userId = USER_ID,
            groupId = GROUP_ID,
            groupName = GROUP_NAME,
            credentials = UserCredentials(
                accessToken = "",
                refreshToken = "",
                expirationDate = Clock.System.now().plus(30.days)
            )
        )
    )

    override suspend fun registerEosUser(request: RegisterEosUserRequest) = Result.Success(
        value = RegisterEosUserResponse(
            userId = USER_ID,
            eosUserId = EOS_USER_ID,
            groupId = GROUP_ID,
            credentials = UserCredentials(
                accessToken = "",
                refreshToken = "",
                expirationDate = Clock.System.now().plus(30.days)
            )
        )
    )

    override suspend fun getAccount() = Result.Success(
        value = CompassAccount(
            userId = USER_ID,
            eosUserId = EOS_USER_ID,
            groupId = GROUP_ID,
            groupName = GROUP_NAME,
            fullName = FULL_NAME,
            avatarUrl = AVATAR_URL_FROM_COMPASS,
            role = UserRole.STUDENT,
            permissions = emptyList(),
        )
    )

    override suspend fun chooseGroup(group: Group) = Result.Success(Unit)

    override suspend fun registerCmt(request: RegisterCmtRequest) = Result.Success(Unit)

    override suspend fun eosAuthToken(login: String, password: String) = Result.Success(
        value = EosAuthResponse(
            rawJson = buildJsonObject {
                putJsonObject("data") {
                    put("studentID", 1)
                    put("fullName", "Some Full Name")
                    put("photoLink", AVATAR_URL_FROM_EOS)
                    putJsonObject("group") {
                        put("item1", "ПрИ-101")
                    }
                }
                put("msg", "Информация о студенте")
            }
        )
    )

    override suspend fun getUserProfile(userId: Int): Result<NetworkUserProfile> {
        TODO()
    }

    override suspend fun updateUserProfile(userProfile: NetworkUserProfile): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun searchMates(
        subjectName: String,
        variant: Int
    ): Result<List<NetworkSearchMateItem>> {
        TODO("Not yet implemented")
    }

    override suspend fun getScheduleVersion(groupId: Int): Result<RemoteDataVersions> {
        TODO("Not yet implemented")
    }

    override suspend fun getSubjects(groupId: Int): Result<List<NetworkSubject>> {
        TODO("Not yet implemented")
    }

    override suspend fun getSchedule(
        groupId: Int,
        fromDate: LocalDate,
        toDate: LocalDate
    ): Result<List<NetworkLesson>> {
        TODO("Not yet implemented")
    }

    override suspend fun refreshToken(refreshToken: String): Result<RefreshTokenResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun userAgreement(): Result<String> {
        TODO("Not yet implemented")
    }

    override suspend fun searchGroups(query: String): Result<List<Group>> {
        TODO("Not yet implemented")
    }

    override suspend fun getChangelog(versionCode: Long): Result<ByteArray> {
        TODO("Not yet implemented")
    }

    override suspend fun searchProfessors(query: String): Result<List<String>> {
        TODO("Not yet implemented")
    }

    override suspend fun getProfessorSchedule(
        professorName: String,
        from: LocalDate,
        to: LocalDate
    ): Result<List<ProfessorClass>> {
        TODO("Not yet implemented")
    }

    override suspend fun getUpdateAvailability(): Result<UpdateAvailabilityResponse> {
        TODO("Not yet implemented")
    }

    companion object {
        const val USER_ID = 1L
        const val EOS_USER_ID = 1L
        const val GROUP_ID = 1
        const val GROUP_NAME = "ПрИ-101"
        const val FULL_NAME = "Some Full Name"
        const val AVATAR_URL_FROM_COMPASS = "https://eos.bgitu.ru/photo/6158.png"
        const val AVATAR_URL_FROM_EOS = "/photo/6158.png"
    }
}