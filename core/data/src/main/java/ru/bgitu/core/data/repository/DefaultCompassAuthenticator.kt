package ru.bgitu.core.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import ru.bgitu.core.data.util.NetworkMonitor
import ru.bgitu.core.common.Result
import ru.bgitu.core.common.getOrElse
import ru.bgitu.core.common.runResulting
import ru.bgitu.core.data.model.AuthStatus
import ru.bgitu.core.data.model.CloudMessagingTokenType
import ru.bgitu.core.data.model.SignInResult
import ru.bgitu.core.database.CompassDatabase
import ru.bgitu.core.datastore.SettingsRepository
import ru.bgitu.core.model.Group
import ru.bgitu.core.model.settings.UserSettings
import ru.bgitu.core.network.BuildConfig
import ru.bgitu.core.network.CompassService
import ru.bgitu.core.network.model.request.RegisterCmtRequest
import ru.bgitu.core.network.model.request.RegisterEosUserRequest
import ru.bgitu.core.network.model.request.RegisterGuestRequest
import ru.bgitu.core.network.model.response.EosAuthResponse

class DefaultCompassAuthenticator internal constructor(
    private val serviceApi: CompassService,
    private val settings: SettingsRepository,
    private val database: CompassDatabase,
    private val networkMonitor: NetworkMonitor,
    private val ioDispatcher: CoroutineDispatcher,
    private val deviceId: String
) : CompassAuthenticator {

    override fun validateAuthentication(): Flow<AuthStatus> = combine(
        networkMonitor.isOnline,
        settings.credentials,
        settings.data
    ) { isOnline, credentials, data ->
        if (!data.isAuthorized || credentials?.isTokenExpired == true) {
            return@combine AuthStatus.SIGNIN_REQUIRED
        }

        if (isOnline) {
            if (settings.getDataVersions().guestAccountVersion < 2) {
                migrateToV2GuestAccount(data)
            }
        }

        if (data.compassAccount != null) {
            AuthStatus.SIGNED
        } else AuthStatus.SIGNED_AS_GUEST
    }

    override suspend fun authWithEos(login: String, password: String): SignInResult {
        val response = serviceApi.eosAuthToken(login, password)
            .getOrElse {
                return SignInResult.Error(it.details)
            }

        return registerEosUser(
            userId = settings.data.first().userId,
            eosAuthResponse = response
        )
    }

    private suspend fun registerEosUser(
        userId: Long,
        eosAuthResponse: EosAuthResponse,
    ): SignInResult {
        val response = serviceApi.registerEosUser(
            RegisterEosUserRequest(
                userId = userId,
                eosUserId = eosAuthResponse.eosUserId,
                eosGroupName = eosAuthResponse.groupName,
                fullName = eosAuthResponse.fullName,
                avatarUrl = BuildConfig.EOS_URL + eosAuthResponse.avatarUrl
            )
        )
            .getOrElse {
                return SignInResult.Error(it.details)
            }

        settings.setCredentials(response.credentials)
        val account = serviceApi.getAccount()
            .getOrElse {
                return SignInResult.Error(it.details)
            }
        settings.setAccountInfo(account)
        if (response.groupId == null) {
            return SignInResult.SuccessButGroupNotFound(
                searchQueryForGroup = eosAuthResponse.groupName
            )
        }
        return SignInResult.Success
    }

    override suspend fun registerGuest(group: Group): SignInResult {
        val response = serviceApi.registerGuest(
            RegisterGuestRequest(
                appUUID = settings.metadata.first().appUuid,
                groupId = group.id,
                groupName = group.name
            )
        )
            .getOrElse {
                return SignInResult.Error(it.details)
            }

        runResulting {
            settings.setUserId(response.userId)
            settings.setGroup(Group(response.groupId, response.groupName))
            settings.setCredentials(response.credentials)
            settings.updateMetadata {
                it.copy(appUuid = deviceId)
            }
        }
            .getOrElse {
                return SignInResult.Error(it.details)
            }

        return SignInResult.Success
    }

    override suspend fun signOut() {
        withContext(ioDispatcher) {
            database.clearAllTables()
            settings.resetToDefaults()
        }
    }

    override suspend fun chooseGroup(group: Group): Result<Unit> {
        return runResulting {
            settings.setGroup(group)
            serviceApi.chooseGroup(group)
        }
    }

    override suspend fun refreshCmt(
        token: String,
        type: CloudMessagingTokenType
    ): Result<Unit> {
        return serviceApi.registerCmt(
            RegisterCmtRequest(
                token = token,
                type = type.toString()
            )
        )
    }

    override suspend fun searhGroups(query: String): Result<List<Group>> {
        return serviceApi.searchGroups(query)
    }

    private suspend fun migrateToV2GuestAccount(data: UserSettings) {
        val group = Group(
            id = data.groupId ?: return,
            name = data.groupName ?: ""
        )
        registerGuest(group)
        settings.updateDataVersions {
            it.copy(guestAccountVersion = 2)
        }
    }
}