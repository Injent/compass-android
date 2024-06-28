package ru.bgitu.feature.login.presentation.recovery

import android.util.Patterns
import androidx.compose.foundation.text.input.TextFieldState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import ru.bgitu.core.common.TextResource
import ru.bgitu.core.common.eventChannel
import ru.bgitu.core.designsystem.util.textAsFlow
import ru.bgitu.feature.login.R

sealed class RecoveryIntent {
    data object Success : RecoveryIntent()
    data class Error(val details: TextResource) : RecoveryIntent()
    data object Loading : RecoveryIntent()
    data object Back : RecoveryIntent()
    data object Submit : RecoveryIntent()
}

sealed class RecoveryEvent {
    data object Back : RecoveryEvent()
    data class Submit(val email: String) : RecoveryEvent()
    data object ShowSuccessDialog : RecoveryEvent()
    data class ShowErrorSnackbar(val errorText: TextResource) : RecoveryEvent()
    data object HideSnackbar : RecoveryEvent()
}

class RecoveryViewModel : ViewModel() {

    private val _events = eventChannel<RecoveryEvent>()
    val events: Flow<RecoveryEvent>
        get() = _events.receiveAsFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    val emailFieldState = TextFieldState()

    @OptIn(FlowPreview::class)
    val isEmailValid = emailFieldState.textAsFlow()
        .debounce(300)
        .mapLatest { text ->
            Patterns.EMAIL_ADDRESS.matcher(text).matches()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = false
        )

    fun onIntent(intent: RecoveryIntent) {
        when (intent) {
            is RecoveryIntent.Error -> {
                _isLoading.value = false
                _events.trySend(
                    RecoveryEvent.ShowErrorSnackbar(intent.details)
                )
            }
            RecoveryIntent.Loading -> {
                _isLoading.value = true
                _events.trySend(RecoveryEvent.HideSnackbar)
            }
            RecoveryIntent.Success -> {
                _isLoading.value = false
                _events.trySend(RecoveryEvent.ShowSuccessDialog)
                _events.trySend(RecoveryEvent.HideSnackbar)
            }
            RecoveryIntent.Back -> {
                _isLoading.value = false
                _events.trySend(RecoveryEvent.HideSnackbar)
                _events.trySend(RecoveryEvent.Back)
            }
            RecoveryIntent.Submit -> {
                _events.trySend(RecoveryEvent.HideSnackbar)
                val trimmedEmail = emailFieldState.text.toString().trim()
                if (Patterns.EMAIL_ADDRESS.matcher(trimmedEmail).matches().not()) {
                    _events.trySend(
                        RecoveryEvent.ShowErrorSnackbar(TextResource.Id(R.string.invalid_email))
                    )
                    return
                }
                _events.trySend(RecoveryEvent.Submit(trimmedEmail))
            }
        }
    }
}