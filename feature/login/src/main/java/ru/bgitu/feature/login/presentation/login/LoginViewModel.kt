package ru.bgitu.feature.login.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import ru.bgitu.components.signin.model.SignInParams
import ru.bgitu.components.signin.model.SignInResult
import ru.bgitu.components.signin.repository.CompassAuthenticator
import ru.bgitu.core.common.R
import ru.bgitu.core.common.TextResource
import ru.bgitu.core.common.eventChannel
import ru.bgitu.core.data.util.NetworkMonitor

data class LoginUiState(
    val error: TextResource? = null,
    val loading: Boolean = false,
)

sealed class LoginIntent {

    data object Back : LoginIntent()
    data class SignIn(val signInParams: SignInParams?) : LoginIntent()
    data object ReadUserAgreement : LoginIntent()
}

sealed class LoginEvent {
    data object Back : LoginEvent()
    data object NavigateToMainScreen : LoginEvent()
    data object ReadUserAgreement : LoginEvent()
    data class ShowErrorSnackbar(val message: TextResource) : LoginEvent()
    data object HideSnackbar : LoginEvent()
}

class LoginViewModel(
    private val compassAuthenticator: CompassAuthenticator,
    private val networkMonitor: NetworkMonitor,
) : ViewModel() {

    private val _events = eventChannel<LoginEvent?>()
    val events = _events.receiveAsFlow()

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            networkMonitor.isOnline
                .onEach { isOnline ->
                    val error = LoginEvent.ShowErrorSnackbar(
                        TextResource.Id(R.string.error_no_internet_connection)
                    )
                    if (isOnline) {
                        _events.trySend(LoginEvent.HideSnackbar)
                    } else {
                        _events.trySend(error)
                    }
                }
                .collect()
        }
    }

    fun onIntent(intent: LoginIntent) {
        when (intent) {
            is LoginIntent.SignIn -> {
                _events.trySend(LoginEvent.HideSnackbar)
                viewModelScope.launch {
                    val result = if (intent.signInParams != null) {
                        compassAuthenticator.signIn(intent.signInParams)
                    } else {
                        compassAuthenticator.signInAnonymously()
                    }

                    if (result is SignInResult.Success) {
                        _events.trySend(LoginEvent.HideSnackbar)
                        _events.send(LoginEvent.NavigateToMainScreen)
                    }
                }
            }
            LoginIntent.ReadUserAgreement -> {
                _events.trySend(LoginEvent.HideSnackbar)
                _events.trySend(LoginEvent.ReadUserAgreement)
            }
            LoginIntent.Back -> {
                _events.trySend(LoginEvent.Back)
            }
        }
    }
}
