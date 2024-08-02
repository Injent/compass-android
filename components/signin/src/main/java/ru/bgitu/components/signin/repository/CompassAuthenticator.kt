package ru.bgitu.components.signin.repository

import kotlinx.coroutines.flow.Flow
import ru.bgitu.components.signin.model.AuthState
import ru.bgitu.components.signin.model.SignInParams
import ru.bgitu.components.signin.model.SignInResult

interface CompassAuthenticator {
    val authState: Flow<AuthState>
    suspend fun signIn(signInParams: SignInParams): SignInResult
    suspend fun signInAnonymously(): SignInResult
    suspend fun signOut()
}