package ru.bgitu.feature.profile_settings.presentation.expert

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.bgitu.feature.profile_settings.data.ProfileRepository
import kotlin.math.exp

class ExpertApplyViewModel(
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private var expertRegistrationJob: Job? = null
    private val _agreementChecked = MutableStateFlow(false)
    val agreementChecked get() = _agreementChecked.asStateFlow()

    fun onCheckAgreement(checked: Boolean) {
        _agreementChecked.value = checked
    }

    fun onContinue() {
        if (expertRegistrationJob?.isActive == true) return
        expertRegistrationJob = viewModelScope.launch {

        }
    }
}