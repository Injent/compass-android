package ru.bgitu.core.testing.repository

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import ru.bgitu.core.data.model.AuthStatus
import ru.bgitu.core.data.model.CloudMessagingTokenType
import ru.bgitu.core.data.model.SignInResult
import ru.bgitu.core.data.repository.CompassAuthenticator
import ru.bgitu.core.model.Group
import ru.bgitu.core.common.Result
import ru.bgitu.core.testing.data.Groups

class TestCompassAuthenticator : CompassAuthenticator {
    private var currentStatus =
        MutableSharedFlow<AuthStatus>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    override fun validateAuthentication(): Flow<AuthStatus> = currentStatus

    override suspend fun registerGuest(group: Group): SignInResult {
        currentStatus.tryEmit(AuthStatus.SIGNED_AS_GUEST)
        return SignInResult.Success
    }

    override suspend fun authWithEos(login: String, password: String): SignInResult {
        currentStatus.tryEmit(AuthStatus.SIGNED)
        return SignInResult.Success
    }

    override suspend fun signOut() {
        currentStatus.tryEmit(AuthStatus.SIGNIN_REQUIRED)
    }

    override suspend fun chooseGroup(group: Group) = Result.Success(Unit)

    override suspend fun refreshCmt(
        token: String,
        type: CloudMessagingTokenType
    ) = Result.Success(Unit)

    override suspend fun searhGroups(query: String): Result<List<Group>> {
        return Result.Success(
            value = Groups.filter { it.name.contains(query, ignoreCase = true) }
        )
    }
}