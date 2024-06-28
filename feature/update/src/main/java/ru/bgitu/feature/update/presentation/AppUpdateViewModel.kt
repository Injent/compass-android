package ru.bgitu.feature.update.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.bgitu.components.updates.api.AppUpdateManager
import ru.bgitu.core.data.repository.CompassRepository
import ru.bgitu.feature.update.model.AppUpdateUiState

class AppUpdateViewModel(
    private val appUpdateManager: AppUpdateManager,
    compassRepository: CompassRepository,
    updateVersionCode: Long
) : ViewModel() {

    private val _uiState = MutableStateFlow(AppUpdateUiState())
    val uiState = _uiState.asStateFlow()

    val changelog = flow {
        compassRepository.getChangelog(updateVersionCode)
            .onSuccess { emit(it) }
            .onFailure { emit(null) }
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null,
        )

    fun onUpdateRequest() {
        _uiState.update {
            it.copy(errorText = null, downloadStarted = false)
        }
        viewModelScope.launch {
            appUpdateManager.startUpdateFlow(appUpdateManager.appUpdateInfo.last())
        }
    }
}