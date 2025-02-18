package ru.bgitu.app.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.bgitu.components.updates.api.AppUpdateManager
import ru.bgitu.components.updates.api.model.InstallState
import ru.bgitu.components.updates.api.model.NativeUpdateInfo
import ru.bgitu.components.updates.api.model.UnknownUpdateInfo
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
    val uiTheme: UiTheme = UiTheme.SYSTEM,
    val updateInfo: UpdateInfo = UnknownUpdateInfo,
    val installState: InstallState = InstallState.Unknown,
    val showUpdateSheet: Boolean = false,
    val avatarUrl: String? = null,
    val shouldShowOnboarding: Boolean = false,
    val helpSiteTraffic: Boolean = false,
    val useDynamicTheme: Boolean = false
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
    private val appUpdateManager: AppUpdateManager,
    settingsRepository: SettingsRepository,
    syncManager: SyncManager,
) : ViewModel() {

    private val _events = eventChannel<MainActivityEvent>()
    val events = _events.receiveAsFlow()

    val uiState = combine(
        settingsRepository.data,
        appUpdateManager.appUpdateInfo,
        appUpdateManager.installState,
    ) { data, updateInfo, installState ->
        val showUpdateSheet = (updateInfo as? NativeUpdateInfo)?.let {
            updateInfo.updateAvailability == UpdateAvailability.UPDATE_AVAILABLE
        } ?: false

        MainActivityUiState(
            isLoading = false,
            uiTheme = data.userPrefs.theme,
            updateInfo = updateInfo,
            installState = installState,
            avatarUrl = data.userProfile?.avatarUrl,
            showUpdateSheet = showUpdateSheet,
            shouldShowOnboarding = data.shouldShowOnboarding,
            helpSiteTraffic = data.userPrefs.helpSiteTraffic,
            useDynamicTheme = data.userPrefs.useDynamicTheme
        )
    }
        .onStart {
            viewModelScope.launch {
                EventBus.subscribe<GlobalAppEvent.ChangePrimaryGroup> {
                    syncManager.requestSync(isManualSync = true)
                }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
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