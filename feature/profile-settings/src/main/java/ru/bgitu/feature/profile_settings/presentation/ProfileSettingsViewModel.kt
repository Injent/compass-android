package ru.bgitu.feature.profile_settings.presentation

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import ru.bgitu.core.datastore.SettingsRepository
import ru.bgitu.core.model.UserProfile

sealed interface ProfileSetttingsUiState {
    data class Success(
        val profile: UserProfile,
    ) : ProfileSetttingsUiState

    data object Loading : ProfileSetttingsUiState
}

sealed class ProfileSettingsIntent {
    data class AddContact(val url: String) : ProfileSettingsIntent()
    data class RemoveContact(val url: String) : ProfileSettingsIntent()
    data class AddVariant(val subjectName: String, val variant: Int) : ProfileSettingsIntent()
    data class RemoveVariant(val subjectName: String) : ProfileSettingsIntent()
    data object Back : ProfileSettingsIntent()
    data object SwitchVisibility : ProfileSettingsIntent()
}

sealed class ProfileSettingsEvent {
    data object Back : ProfileSettingsEvent()
}

class ProfileSettingsViewModel(
    private val settingsRepository: SettingsRepository
) : ViewModel() {
    private val _events = Channel<ProfileSettingsEvent>()
    val events = _events.receiveAsFlow()

    private val _profile = MutableStateFlow(
        runBlocking { settingsRepository.getProfile() }
    )
    val profile = _profile.asStateFlow()


    val bioField = TextFieldState()
    val nameField = TextFieldState()
    val lastNameField = TextFieldState()

    init {
        bioField.setTextAndPlaceCursorAtEnd(_profile.value.bio)
        lastNameField.setTextAndPlaceCursorAtEnd(_profile.value.lastName)
        nameField.setTextAndPlaceCursorAtEnd(_profile.value.firstName)
    }

    fun onIntent(intent: ProfileSettingsIntent) {
        when (intent) {
            is ProfileSettingsIntent.AddContact -> TODO()
            is ProfileSettingsIntent.AddVariant -> TODO()
            is ProfileSettingsIntent.RemoveContact -> TODO()
            is ProfileSettingsIntent.RemoveVariant -> TODO()
            is ProfileSettingsIntent.Back -> viewModelScope.launch { updateProfile() }
            is ProfileSettingsIntent.SwitchVisibility -> _profile.update {
                it.copy(publicProfile = !it.publicProfile)
            }
        }
    }

    private suspend fun updateProfile() {
        val fetchedProfile = _profile.value.copy(
            bio = bioField.text.toString(),
            lastName = lastNameField.text.toString(),
            firstName = nameField.text.toString()
        )
        settingsRepository.updateProfile { fetchedProfile }
        _events.trySend(ProfileSettingsEvent.Back)
    }
}