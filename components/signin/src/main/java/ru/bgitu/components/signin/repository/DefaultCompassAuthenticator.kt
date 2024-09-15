package ru.bgitu.components.signin.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import ru.bgitu.components.signin.model.AuthState
import ru.bgitu.components.signin.model.SignInParams
import ru.bgitu.components.signin.model.SignInResult
import ru.bgitu.core.common.getOrElse
import ru.bgitu.core.data.util.NetworkMonitor
import ru.bgitu.core.database.CompassDatabase
import ru.bgitu.core.datastore.SettingsRepository
import ru.bgitu.core.model.settings.UserCredentials
import ru.bgitu.core.network.CompassService
import ru.bgitu.core.network.model.request.ExternalAuthRequest

class DefaultCompassAuthenticator(
    private val compassService: CompassService,
    private val settingsRepository: SettingsRepository,
    private val compassDatabase: CompassDatabase,
    networkMonitor: NetworkMonitor
) : CompassAuthenticator {

    override val authState: Flow<AuthState> = combine(
        settingsRepository.data.distinctUntilChanged { old, new ->
            (old.isAuthorized != new.isAuthorized) || (old.isAnonymous != new.isAnonymous)
        },
        networkMonitor.isOnline
    ) { userData, isOnline ->
        if (userData.isAuthorized) {
            if (isOnline) {
                refreshTokenIfNeeded()
            }
            AuthState.AUTHED
        } else {
            if (userData.isAnonymous) {
                AuthState.ANONYMOUS
            } else {
                AuthState.NOT_AUTHED
            }
        }
    }
        .onStart { emit(AuthState.LOADING) }

    override suspend fun signIn(signInParams: SignInParams): SignInResult {
        val authResponse = compassService.authWithExternalService(
            request = signInParams.toRequest()
        ).getOrElse { return SignInResult.Error(it.details) }

        settingsRepository.setAuthData(
            userId = authResponse.userId,
            credentials = UserCredentials(
                accessToken = authResponse.accessToken,
                refreshToken = authResponse.refreshToken
            )
        )
        return SignInResult.Success
    }

    override suspend fun signInAnonymously(): SignInResult {
        settingsRepository.updateMetadata {
            it.copy(isAnonymousUser = true)
        }
        return SignInResult.Success
    }

    override suspend fun signOut() = withContext(Dispatchers.IO) {
        settingsRepository.clearUserData()
        compassDatabase.clearAllTables()
    }

    private suspend fun refreshTokenIfNeeded() {
        if (settingsRepository.getLastAuthDate() < Clock.System.now()) {
            return
        }
        val refreshToken = settingsRepository.credentials.first()?.refreshToken ?: return

        val credentials = compassService.refreshToken(refreshToken)
            .getOrElse { return }

        settingsRepository.setCredentials(
            userCredentials = UserCredentials(
                accessToken = credentials.accessToken,
                refreshToken = credentials.refreshToken
            )
        )
    }
}

private fun SignInParams.toRequest() = ExternalAuthRequest(
    authMethod = authMethod.name,
    idToken = idToken,
)