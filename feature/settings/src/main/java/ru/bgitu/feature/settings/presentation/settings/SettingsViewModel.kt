package ru.bgitu.feature.settings.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.bgitu.core.common.eventChannel
import ru.bgitu.core.datastore.SettingsRepository
import ru.bgitu.core.model.settings.UiTheme
import ru.bgitu.core.model.settings.UserPrefs
import ru.bgitu.core.model.settings.UserSettings
import ru.bgitu.feature.schedule_notifier.api.ScheduleNotifier

sealed interface SettingsUiState {
    data class Success(
        val prefs: UserPrefs,
        val soundName: String? = null
    ) : SettingsUiState
    data object Loading : SettingsUiState
}

sealed interface SettingsIntent {
    data class IgnoreMinorUpdate(val ignoreUpdates: Boolean) : SettingsIntent
    data class ChangeUiTheme(val uiTheme: UiTheme) : SettingsIntent
    data class SwitchScheduleNotifier(val enabled: Boolean) : SettingsIntent
    data object NavigateBack : SettingsIntent
    data object NavigateToAbout : SettingsIntent
    data object NavigateToHelp : SettingsIntent
}

sealed interface SettingsEvent {
    data object NavigateBack : SettingsEvent
    data object NavigateToAbout : SettingsEvent
    data object NavigateToHelp : SettingsEvent
}

class SettingsViewModel(
    private val settings: SettingsRepository,
    private val scheduleNotifier: ScheduleNotifier
) : ViewModel() {
    private val _events = eventChannel<SettingsEvent>()
    val events = _events.receiveAsFlow()

    val settingsUiState = settings.data
        .mapLatest { it.toUiState() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = SettingsUiState.Loading
        )

    fun onIntent(intent: SettingsIntent) {
        viewModelScope.launch {
            when (intent) {
                is SettingsIntent.IgnoreMinorUpdate -> {
                    settings.updateUserPrefs {
                        it.copy(ignoreMinorUpdates = intent.ignoreUpdates)
                    }
                }
                is SettingsIntent.ChangeUiTheme -> {
                    settings.updateUserPrefs {
                        it.copy(theme = intent.uiTheme)
                    }
                }
                SettingsIntent.NavigateBack -> {
                    _events.send(SettingsEvent.NavigateBack)
                }
                is SettingsIntent.SwitchScheduleNotifier -> {
                    if (intent.enabled) {
                        scheduleNotifier.enable()
                    } else scheduleNotifier.disable()
                }
                SettingsIntent.NavigateToAbout -> {
                    _events.trySend(SettingsEvent.NavigateToAbout)
                }
                SettingsIntent.NavigateToHelp -> {
                    _events.trySend(SettingsEvent.NavigateToHelp)
                }
            }
        }
    }
}

private fun UserSettings.toUiState(): SettingsUiState.Success {
    return SettingsUiState.Success(
        prefs = this.userPrefs,
    )
}