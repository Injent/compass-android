package ru.bgitu.feature.profile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.bgitu.components.signin.repository.CompassAuthenticator
import ru.bgitu.core.common.eventChannel
import ru.bgitu.core.common.eventbus.EventBus
import ru.bgitu.core.common.eventbus.GlobalAppEvent
import ru.bgitu.core.datastore.SettingsRepository
import ru.bgitu.core.model.UserProfile

sealed interface ProfileUiState {
    data object Loading : ProfileUiState
    data class Empty(
        val isMateBannerVisible: Boolean
    ) : ProfileUiState
    data class Success(val profile: UserProfile) : ProfileUiState
    data class Error(val profile: UserProfile) : ProfileUiState
}

sealed interface ProfileIntent {
    data object SignOut : ProfileIntent
    data object NavigateToSettings : ProfileIntent
    data object NavigateToProfileSettings : ProfileIntent
    data object NavigateToAboutApp : ProfileIntent
    data object NavigateToHelp : ProfileIntent
    data object NavigateToLogin : ProfileIntent
    data object NavigateToGroups : ProfileIntent
    data object CloseMateBanner : ProfileIntent

}

sealed interface ProfileEvent {
    data class NavigateToLogin(val autoSignOut: Boolean) : ProfileEvent
    data object NavigateToSettings : ProfileEvent
    data object NavigatoToProfileSettings : ProfileEvent
    data object NavigateToHelp : ProfileEvent
    data object NavigateToAboutApp : ProfileEvent
    data object NavigateToGroups : ProfileEvent
}

class ProfileViewModel(
    private val settingsRepository: SettingsRepository,
    private val compassAuthenticator: CompassAuthenticator
) : ViewModel() {

    private val _events = eventChannel<ProfileEvent>()
    val events = _events.receiveAsFlow()

    val profile = combine(
        settingsRepository.data,
        settingsRepository.metadata
    ) { userData, metaData ->
        if (userData.userProfile == null && userData.isAnonymous) {
            return@combine ProfileUiState.Empty(isMateBannerVisible = !metaData.isMateBannerClosed)
        }
        userData.userProfile?.let { profile ->
            ProfileUiState.Success(profile)
        } ?: ProfileUiState.Empty(false)

    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ProfileUiState.Loading
        )

    fun onIntent(intent: ProfileIntent) {
        when (intent) {
            ProfileIntent.SignOut -> viewModelScope.launch {
                compassAuthenticator.signOut()
                EventBus.post(GlobalAppEvent.SignOut)
                _events.send(ProfileEvent.NavigateToLogin(autoSignOut = true))
            }
            ProfileIntent.NavigateToSettings -> _events.trySend(ProfileEvent.NavigateToSettings)
            ProfileIntent.NavigateToProfileSettings -> _events.trySend(
                ProfileEvent.NavigatoToProfileSettings
            )
            ProfileIntent.NavigateToAboutApp -> _events.trySend(ProfileEvent.NavigateToAboutApp)
            ProfileIntent.NavigateToHelp -> _events.trySend(ProfileEvent.NavigateToHelp)
            ProfileIntent.NavigateToLogin -> _events.trySend(
                ProfileEvent.NavigateToLogin(autoSignOut = false)
            )
            ProfileIntent.NavigateToGroups -> _events.trySend(ProfileEvent.NavigateToGroups)
            ProfileIntent.CloseMateBanner -> viewModelScope.launch {
                settingsRepository.updateMetadata {
                    it.copy(isMateBannerClosed = true)
                }
            }
        }
    }
}