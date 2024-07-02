package ru.bgitu.feature.profile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.bgitu.core.common.eventChannel
import ru.bgitu.core.common.getOrElse
import ru.bgitu.core.data.repository.CompassAuthenticator
import ru.bgitu.core.data.repository.CompassRepository
import ru.bgitu.core.domain.GetUserSettingsUseCase
import ru.bgitu.core.model.UserProfile
import ru.bgitu.feature.schedule_notifier.api.ScheduleNotifier

sealed interface ProfileUiState {
    data object Loading : ProfileUiState
    data class Empty(
        val groupName: String
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

}

sealed interface ProfileEvent {
    data class NavigateToLogin(val signout: Boolean) : ProfileEvent
    data object NavigateToSettings : ProfileEvent
    data object NavigatoToProfileSettings : ProfileEvent
    data object NavigateToHelp : ProfileEvent
    data object NavigateToAboutApp : ProfileEvent
    data object NavigateToGroups : ProfileEvent
}

class ProfileViewModel(
    private val authenticator: CompassAuthenticator,
    private val scheduleNotifier: ScheduleNotifier,
    private val compassRepository: CompassRepository,
    getUserSettings: GetUserSettingsUseCase
) : ViewModel() {

    private val _events = eventChannel<ProfileEvent>()
    val events = _events.receiveAsFlow()

    val profile = getUserSettings().mapLatest { data ->
        return@mapLatest ProfileUiState.Empty(groupName = data.groupName ?: "")
        val account = data.compassAccount

        if (account == null) {
            ProfileUiState.Empty(groupName = data.groupName ?: "")
        } else {
            val profile = compassRepository.getUserProfile(account.userId.toInt())
                .getOrElse {
                    return@mapLatest ProfileUiState.Error(
                        UserProfile(
                            userId = account.userId,
                            bio = "",
                            avatarUrl = account.avatarUrl,
                            firstName = account.fullName.split(" ").last(),
                            lastName = account.fullName.split(" ").first(),
                            contacts = null,
                            publicProfile = true,
                            variants = emptyList(),
                        )
                    )
                }
            ProfileUiState.Success(profile)
        }
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ProfileUiState.Loading
        )

    fun onIntent(intent: ProfileIntent) {
        when (intent) {
            ProfileIntent.SignOut -> {
                viewModelScope.launch {
                    signOut()
                    _events.send(ProfileEvent.NavigateToLogin(signout = true))
                }
            }
            ProfileIntent.NavigateToSettings -> {
                _events.trySend(ProfileEvent.NavigateToSettings)
            }
            ProfileIntent.NavigateToProfileSettings -> {
                _events.trySend(ProfileEvent.NavigatoToProfileSettings)
            }
            ProfileIntent.NavigateToAboutApp -> {
                _events.trySend(ProfileEvent.NavigateToAboutApp)
            }
            ProfileIntent.NavigateToHelp -> {
                _events.trySend(ProfileEvent.NavigateToHelp)
            }
            ProfileIntent.NavigateToLogin -> {
                _events.trySend(ProfileEvent.NavigateToLogin(signout = false))
            }
            ProfileIntent.NavigateToGroups -> {
                _events.trySend(ProfileEvent.NavigateToGroups)
            }
        }
    }

    private suspend fun signOut() {
        scheduleNotifier.disable()
        authenticator.signOut()
    }
}