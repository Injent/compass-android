package ru.bgitu.core.data.repository

import kotlinx.coroutines.flow.Flow
import ru.bgitu.core.common.Result
import ru.bgitu.core.data.model.AuthStatus
import ru.bgitu.core.data.model.CloudMessagingTokenType
import ru.bgitu.core.data.model.SignInResult
import ru.bgitu.core.model.Group

interface CompassAuthenticator {
    fun validateAuthentication(): Flow<AuthStatus>
    suspend fun registerGuest(group: Group): SignInResult
    suspend fun authWithEos(login: String, password: String): SignInResult
    suspend fun signOut()
    suspend fun chooseGroup(group: Group): Result<Unit>
    suspend fun refreshCmt(token: String, type: CloudMessagingTokenType): Result<Unit>
    suspend fun searhGroups(query: String): Result<List<Group>>
}