package ru.bgitu.feature.profile_settings.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import ru.bgitu.core.datastore.SettingsRepository

sealed interface ProfileSettingsIntent {
    data class SetDisplayName(val lastName: String) : ProfileSettingsIntent
    data class SetBio(val bio: String) : ProfileSettingsIntent
    data class AddContact(val url: String) : ProfileSettingsIntent
    data class RemoveContact(val url: String) : ProfileSettingsIntent
    data class AddVariant(val subjectName: String, val variant: Int) : ProfileSettingsIntent
    data class RemoveVariant(val subjectName: String) : ProfileSettingsIntent
    data object Back : ProfileSettingsIntent
    data object NavigateToExpertApply : ProfileSettingsIntent
    data class SetPublicProfile(val isVisible: Boolean) : ProfileSettingsIntent
    data class SetProfileImage(val avatarUrl: String?) : ProfileSettingsIntent
}

sealed interface ProfileSettingsEvent {
    data object Back : ProfileSettingsEvent
    data object NavigateToExpertApply : ProfileSettingsEvent
}

class ProfileSettingsViewModel(
    private val settingsRepository: SettingsRepository
) : ViewModel() {
    private val _events = Channel<ProfileSettingsEvent>()
    val events = _events.receiveAsFlow()

    val profile = settingsRepository.data.mapLatest {
        checkNotNull(it.userProfile)
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = runBlocking {
                checkNotNull(settingsRepository.getProfile()) {
                    "Profile shouldn't ne null when navigating to profile settings"
                }
            }
        )

    fun onIntent(intent: ProfileSettingsIntent) {
        when (intent) {
            is ProfileSettingsIntent.AddContact -> TODO()
            is ProfileSettingsIntent.AddVariant -> TODO()
            is ProfileSettingsIntent.RemoveContact -> TODO()
            is ProfileSettingsIntent.RemoveVariant -> TODO()
            is ProfileSettingsIntent.Back -> _events.trySend(ProfileSettingsEvent.Back)
            is ProfileSettingsIntent.SetPublicProfile -> viewModelScope.launch {
                settingsRepository.updateProfile {
                    it.copy(publicProfile = intent.isVisible)
                }
            }
            is ProfileSettingsIntent.SetDisplayName -> viewModelScope.launch {
                settingsRepository.updateProfile {
                    it.copy(displayName = intent.lastName)
                }
            }
            is ProfileSettingsIntent.SetBio -> viewModelScope.launch {
                settingsRepository.updateProfile {
                    it.copy(bio = intent.bio.trim())
                }
            }
            is ProfileSettingsIntent.SetProfileImage -> viewModelScope.launch {
                settingsRepository.updateProfile {
                    it.copy(avatarUrl = intent.avatarUrl)
                }
            }
            is ProfileSettingsIntent.NavigateToExpertApply -> {
                _events.trySend(ProfileSettingsEvent.NavigateToExpertApply)
            }
        }
    }
}