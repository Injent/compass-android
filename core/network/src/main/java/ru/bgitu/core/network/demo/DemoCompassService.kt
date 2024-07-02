package ru.bgitu.core.network.demo

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlinx.serialization.json.putJsonObject
import ru.bgitu.core.common.Result
import ru.bgitu.core.common.STUDENT_MARKER
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

class DemoCompassService : CompassService {
    override suspend fun getUserProfile(userId: Int): Result<NetworkUserProfile> {
        TODO("Not yet implemented")
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

    override suspend fun registerGuest(request: RegisterGuestRequest): Result<RegisterGuestAuthResponse> {
        return Result.Success(
            value = RegisterGuestAuthResponse(
                userId = USER_ID,
                groupId = GROUP_ID,
                groupName = GROUP_NAME,
                credentials = UserCredentials(
                    accessToken = ACCESS_TOKEN,
                    refreshToken = REFRESH_TOKEN,
                    expirationDate = Clock.System.now().plus(7.days),
                    authDate = Clock.System.now()
                )
            )
        )
    }

    override suspend fun registerEosUser(request: RegisterEosUserRequest): Result<RegisterEosUserResponse> {
        return Result.Success(
            value = RegisterEosUserResponse(
                userId = USER_ID,
                eosUserId = EOS_USER_ID,
                groupId = GROUP_ID,
                credentials = UserCredentials(
                    accessToken = ACCESS_TOKEN,
                    refreshToken = REFRESH_TOKEN,
                    expirationDate = Clock.System.now().plus(7.days),
                    authDate = Clock.System.now()
                )
            )
        )
    }

    override suspend fun chooseGroup(group: Group): Result<Unit> {
        return Result.Success(Unit)
    }

    override suspend fun getAccount(): Result<CompassAccount> {
        return Result.Success(
            value = CompassAccount(
                userId = USER_ID,
                eosUserId = EOS_USER_ID,
                groupId = GROUP_ID,
                groupName = GROUP_NAME,
                fullName = FULL_NAME,
                avatarUrl = null,
                role = UserRole.STUDENT,
                permissions = emptyList()
            )
        )
    }

    override suspend fun eosAuthToken(login: String, password: String): Result<EosAuthResponse> {
        return Result.Success(
            value = EosAuthResponse(
                rawJson = buildJsonObject {
                    putJsonObject("data") {
                        put("studentID", EOS_USER_ID)
                        put("fullName", FULL_NAME)
                        put("photoLink", EOS_AVATAR_URL)
                        putJsonObject("group") {
                            put("item1", GROUP_NAME)
                        }
                    }
                    put("msg", STUDENT_MARKER)
                }
            )
        )
    }

    override suspend fun getScheduleVersion(groupId: Int): Result<RemoteDataVersions> {
        return Result.Success(
            RemoteDataVersions(
                scheduleVersion = Int.MAX_VALUE,
                lastForceUpdateVersion = -1
            )
        )
    }

    override suspend fun getSubjects(groupId: Int): Result<List<NetworkSubject>> {
        return Result.Success(demoSubjects)
    }

    override suspend fun getSchedule(
        groupId: Int,
        fromDate: LocalDate,
        toDate: LocalDate
    ): Result<List<NetworkLesson>> {
        return Result.Success(
            demoLessons.filter {
                it.date in fromDate..toDate
            }
        )
    }

    override suspend fun registerCmt(request: RegisterCmtRequest): Result<Unit> {
        return Result.Success(Unit)
    }

    override suspend fun refreshToken(refreshToken: String): Result<RefreshTokenResponse> {
        return Result.Success(
            RefreshTokenResponse(
                accessToken = ACCESS_TOKEN,
                refreshToken = REFRESH_TOKEN,
                expirationDate = Clock.System.now().plus(7.days)
            )
        )
    }

    override suspend fun userAgreement(): Result<String> {
        return Result.Success("User Agreement text")
    }

    override suspend fun searchGroups(query: String): Result<List<Group>> {
        return Result.Success(demoGroups)
    }

    override suspend fun getChangelog(versionCode: Long): Result<ByteArray> {
        return Result.Success("Changes text".toByteArray())
    }

    override suspend fun searchProfessors(query: String): Result<List<String>> {
        return Result.Success(
            demoTeachers.filter {
                query in it
            }
        )
    }

    override suspend fun getProfessorSchedule(
        professorName: String,
        from: LocalDate,
        to: LocalDate
    ): Result<List<ProfessorClass>> {
        return Result.Success(
            teachersSchedule[professorName]?.filter { it.date in from..to }
                ?: emptyList()
        )
    }

    override suspend fun getUpdateAvailability(): Result<UpdateAvailabilityResponse> {
        return Result.Success(
            UpdateAvailabilityResponse(
                versionCode = 0,
                forceUpdateVersions = emptyList(),
                downloadUrl = "",
                size = 0,
                checksum = ""
            )
        )
    }

    companion object {
        const val USER_ID = 1L
        const val EOS_USER_ID = 2L
        const val GROUP_NAME = "GRO-101"
        const val GROUP_ID = 1
        const val REFRESH_TOKEN = "refresh token"
        const val ACCESS_TOKEN = "access token"
        const val EOS_AVATAR_URL = "/photo653.png"
        const val FULL_NAME = "Verevkin Elisey Andreevich"
    }
}