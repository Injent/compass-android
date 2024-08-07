package ru.bgitu.app.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.bgitu.components.signin.model.AuthState
import ru.bgitu.components.signin.repository.CompassAuthenticator
import ru.bgitu.components.sync.workers.SyncWorker
import ru.bgitu.components.updates.api.AppUpdateManager
import ru.bgitu.components.updates.api.model.InstallState
import ru.bgitu.components.updates.api.model.UpdateAvailability
import ru.bgitu.components.updates.api.model.UpdateInfo
import ru.bgitu.core.common.TextResource
import ru.bgitu.core.common.eventChannel
import ru.bgitu.core.common.eventbus.EventBus
import ru.bgitu.core.common.eventbus.GlobalAppEvent
import ru.bgitu.core.data.util.SyncManager
import ru.bgitu.core.datastore.SettingsRepository
import ru.bgitu.core.model.settings.UiTheme

data class MainActivityUiState(
    val isLoading: Boolean = true,
    val authState: AuthState = AuthState.LOADING,
    val uiTheme: UiTheme = UiTheme.SYSTEM,
    val updateInfo: UpdateInfo = UpdateInfo.Unknown,
    val installState: InstallState = InstallState.Unknown,
    val showUpdateSheet: Boolean = false,
    val avatarUrl: String? = null,
)

sealed interface MainActivityIntent {
    data object RetryDownload : MainActivityIntent
    data object InstallUpdate : MainActivityIntent
}

sealed interface MainActivityEvent {
    data class ShowErrorSnackbar(val details: TextResource) : MainActivityEvent
    data object HideSnackBar : MainActivityEvent
}

class MainViewModel(
    compassAuthenticator: CompassAuthenticator,
    private val appUpdateManager: AppUpdateManager,
    settingsRepository: SettingsRepository,
    syncManager: SyncManager
) : ViewModel() {

    private val _events = eventChannel<MainActivityEvent>()
    val events = _events.receiveAsFlow()

    init {
        viewModelScope.launch {
            EventBus.subscribe<GlobalAppEvent.ChangeGroup> {
                syncManager.requestSync()
            }
        }
    }

    val uiState = combine(
        compassAuthenticator.authState,
        settingsRepository.data,
        appUpdateManager.appUpdateInfo,
        appUpdateManager.installState,
    ) { authState, data, updateInfo, installState ->
        val showUpdateSheet = (updateInfo as? UpdateInfo.NativeUpdate)?.let {
            updateInfo.updateAvailability == UpdateAvailability.UPDATE_AVAILABLE
        } ?: false

        MainActivityUiState(
            isLoading = authState == AuthState.LOADING,
            authState = authState,
            uiTheme = data.userPrefs.theme,
            updateInfo = updateInfo,
            installState = installState,
            avatarUrl = data.userProfile?.avatarUrl,
            showUpdateSheet = showUpdateSheet
        )
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
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