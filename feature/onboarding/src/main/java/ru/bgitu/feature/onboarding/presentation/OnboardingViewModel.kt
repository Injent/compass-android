package ru.bgitu.feature.onboarding.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.bgitu.core.datastore.SettingsRepository

class OnboardingViewModel(
    private val settingsRepository: SettingsRepository
) : ViewModel() {
    private var skipJob: Job? = null

    val shouldShowOnboarding = settingsRepository.metadata.mapLatest { it.shouldShowOnboarding }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = false
        )

    fun onSkip() {
        if (skipJob?.isActive == true) return

        skipJob = viewModelScope.launch {
            settingsRepository.updateMetadata {
                it.copy(shouldShowOnboarding = false)
            }
        }
    }
}