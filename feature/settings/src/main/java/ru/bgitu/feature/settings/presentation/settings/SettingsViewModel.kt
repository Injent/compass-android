package ru.bgitu.feature.settings.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime
import ru.bgitu.core.common.eventChannel
import ru.bgitu.core.data.util.SyncManager
import ru.bgitu.core.datastore.SettingsRepository
import ru.bgitu.core.model.settings.UiTheme
import ru.bgitu.core.model.settings.UserPrefs
import ru.bgitu.feature.schedule_notifier.api.ScheduleNotifier

sealed interface SettingsUiState {
    data class Success(
        val prefs: UserPrefs,
        val isGroupSelected: Boolean,
        val nextScheduleNotification: LocalDateTime?,
    ) : SettingsUiState
    data object Loading : SettingsUiState
}

sealed interface SettingsIntent {
    data class ChangeUiTheme(val uiTheme: UiTheme) : SettingsIntent
    data class SwitchScheduleNotifier(val enabled: Boolean) : SettingsIntent
    data object NavigateBack : SettingsIntent
    data object NavigateToAbout : SettingsIntent
    data object NavigateToHelp : SettingsIntent
    data object NavigateToGroups : SettingsIntent
}

sealed interface SettingsEvent {
    data object NavigateBack : SettingsEvent
    data object NavigateToAbout : SettingsEvent
    data object NavigateToHelp : SettingsEvent
    data object NavigateToGroups : SettingsEvent
}

class SettingsViewModel(
    private val settings: SettingsRepository,
    private val scheduleNotifier: ScheduleNotifier,
) : ViewModel() {
    private val _events = eventChannel<SettingsEvent>()
    val events = _events.receiveAsFlow()

    val settingsUiState = combine(
        settings.data,
        settings.metadata
    ) { userData, metadata ->
        SettingsUiState.Success(
            prefs = userData.userPrefs,
            isGroupSelected = userData.primaryGroup != null,
            nextScheduleNotification = metadata.scheduleNotifierAlarmDateTime.takeIf {
                userData.userPrefs.showPinnedSchedule
            }
        )
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = SettingsUiState.Loading
        )

    fun onIntent(intent: SettingsIntent) {
        viewModelScope.launch {
            when (intent) {
                is SettingsIntent.ChangeUiTheme -> {
                    settings.updateUserPrefs {
                        it.copy(theme = intent.uiTheme)
                    }
                }
                SettingsIntent.NavigateBack -> _events.send(SettingsEvent.NavigateBack)
                is SettingsIntent.SwitchScheduleNotifier -> {
                    if (intent.enabled) {
                        scheduleNotifier.enable()
                    } else scheduleNotifier.disable()
                }
                SettingsIntent.NavigateToAbout -> _events.trySend(SettingsEvent.NavigateToAbout)
                SettingsIntent.NavigateToHelp -> _events.trySend(SettingsEvent.NavigateToHelp)
                SettingsIntent.NavigateToGroups -> _events.trySend(SettingsEvent.NavigateToGroups)
            }
        }
    }
}