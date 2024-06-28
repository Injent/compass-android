package ru.bgitu.feature.login.presentation.login

import androidx.compose.foundation.text.input.TextFieldState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.bgitu.core.common.R
import ru.bgitu.core.common.TextResource
import ru.bgitu.core.common.eventChannel
import ru.bgitu.core.data.model.SignInResult
import ru.bgitu.core.data.repository.CompassAuthenticator
import ru.bgitu.core.data.util.NetworkMonitor
import ru.bgitu.core.data.util.SyncManager
import ru.bgitu.core.designsystem.util.textAsFlow

data class LoginUiState(
    val error: TextResource? = null,
    val loading: Boolean = false,
    val passwordVisible: Boolean = false,
    val verificationRequest: Boolean = false,
    val canLogin: Boolean = false,
)

sealed class LoginIntent {

    data object Back : LoginIntent()
    data object Login : LoginIntent()
    data object ReadUserAgreement : LoginIntent()
    data object NavigateToGroupSelection : LoginIntent()
    data object SwitchPasswordVisibility : LoginIntent()
    data object ForgotPassword : LoginIntent()
}

sealed class LoginEvent {
    data object Back : LoginEvent()
    data object NavigateToMainScreen : LoginEvent()
    data object ReadUserAgreement : LoginEvent()
    data class ShowErrorSnackbar(val message: TextResource) : LoginEvent()
    data object HideSnackbar : LoginEvent()
    data class NavigateToGroupSelection(val searchQuery: String? = null) : LoginEvent()
    data object ForgotPassword : LoginEvent()
}

class LoginViewModel(
    private val compassAuthenticator: CompassAuthenticator,
    private val syncManager: SyncManager,
    private val networkMonitor: NetworkMonitor,
    verificationRequest: Boolean
) : ViewModel() {

    private val _events = eventChannel<LoginEvent?>()
    val events = _events.receiveAsFlow()

    val loginFieldState = TextFieldState()
    val passwordFieldState = TextFieldState()

    private val _uiState = MutableStateFlow(LoginUiState(verificationRequest = verificationRequest))
    val uiState: StateFlow<LoginUiState> = combine(
        _uiState,
        loginFieldState.textAsFlow(),
        passwordFieldState.textAsFlow()
    ) { uiState, login, password ->
        uiState.copy(
            canLogin = login.isNotEmpty() && password.isNotEmpty()
        )
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = _uiState.value
        )

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
            LoginIntent.Login -> {
                _events.trySend(LoginEvent.HideSnackbar)
                viewModelScope.launch {
                    authWithEos(
                        login = loginFieldState.text.toString(),
                        password = passwordFieldState.text.toString()
                    )
                }
            }
            LoginIntent.ReadUserAgreement -> {
                _events.trySend(LoginEvent.HideSnackbar)
                _events.trySend(LoginEvent.ReadUserAgreement)
            }
            LoginIntent.NavigateToGroupSelection -> {
                _events.trySend(LoginEvent.HideSnackbar)
                _events.trySend(LoginEvent.NavigateToGroupSelection())
            }
            LoginIntent.SwitchPasswordVisibility -> {
                _uiState.update {
                    it.copy(passwordVisible = it.passwordVisible.not())
                }
            }
            LoginIntent.Back -> {
                _events.trySend(LoginEvent.Back)
            }
            LoginIntent.ForgotPassword -> {
                _events.trySend(LoginEvent.ForgotPassword)
            }
        }
    }

    private suspend fun authWithEos(login: String, password: String) {
        _uiState.update {
            it.copy(loading = true)
        }

        when (val result = compassAuthenticator.authWithEos(login.trim(), password.trim())) {
            is SignInResult.Error -> {
                _events.send(LoginEvent.ShowErrorSnackbar(result.details))
            }
            SignInResult.Success -> {
                syncManager.fullSync()
                _events.send(LoginEvent.NavigateToMainScreen)
            }
            is SignInResult.SuccessButGroupNotFound -> {
                _events.send(LoginEvent.NavigateToGroupSelection(result.searchQueryForGroup))
            }
        }
        _uiState.update {
            it.copy(loading = false)
        }
    }
}
