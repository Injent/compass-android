package ru.bgitu.app.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.bgitu.components.updates.api.AppUpdateManager
import ru.bgitu.components.updates.api.model.InstallState
import ru.bgitu.components.updates.api.model.UpdateAvailability
import ru.bgitu.components.updates.api.model.UpdateInfo
import ru.bgitu.core.common.TextResource
import ru.bgitu.core.common.eventChannel
import ru.bgitu.core.data.model.AuthStatus
import ru.bgitu.core.data.repository.CompassAuthenticator
import ru.bgitu.core.datastore.SettingsRepository
import ru.bgitu.core.model.settings.UiTheme

data class MainActivityUiState(
    val isLoading: Boolean = true,
    val authStatus: AuthStatus = AuthStatus.LOADING,
    val uiTheme: UiTheme = UiTheme.SYSTEM,
    val updateInfo: UpdateInfo = UpdateInfo.Unknown,
    val installState: InstallState = InstallState.Unknown,
    val showUpdateSheet: Boolean = false,
    val avatarUrl: String? = null
)

sealed class MainActivityIntent {
    data object RetryDownload : MainActivityIntent()
    data object InstallUpdate : MainActivityIntent()
}

sealed class MainActivityEvent {
    data class ShowErrorSnackbar(val details: TextResource) : MainActivityEvent()
    data object HideSnackBar : MainActivityEvent()
}

class MainViewModel(
    compassAuthenticator: CompassAuthenticator,
    private val appUpdateManager: AppUpdateManager,
    settings: SettingsRepository,
) : ViewModel() {

    private val _events = eventChannel<MainActivityEvent>()
    val events = _events.receiveAsFlow()

    val uiState = combine(
        compassAuthenticator.validateAuthentication(),
        settings.data,
        appUpdateManager.appUpdateInfo,
        appUpdateManager.installState
    ) { authStatus, data, updateInfo, installState ->
        MainActivityUiState(
            isLoading = authStatus == AuthStatus.LOADING,
            authStatus = authStatus,
            uiTheme = data.userPrefs.theme,
            updateInfo = updateInfo,
            installState = installState,
            avatarUrl = data.compassAccount?.avatarUrl,
            showUpdateSheet = updateInfo is UpdateInfo.NativeUpdate
                    && updateInfo.updateAvailability == UpdateAvailability.UPDATE_AVAILABLE
        )
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = MainActivityUiState()
        )

    fun onIntent(intent: MainActivityIntent) {
        when (intent) {
            MainActivityIntent.InstallUpdate -> {
                viewModelScope.launch {
                    installUpdate()
                }
            }
            MainActivityIntent.RetryDownload -> {
                retryUpdate()
            }
        }
    }

    private fun retryUpdate() {
        _events.trySend(MainActivityEvent.HideSnackBar)
        appUpdateManager.startUpdateFlow(uiState.value.updateInfo)
    }

    private suspend fun installUpdate() {
        _events.trySend(MainActivityEvent.HideSnackBar)
        appUpdateManager.completeUpdate()
            .onFailure {
                _events.trySend(MainActivityEvent.ShowErrorSnackbar(it.details))
            }
    }
}