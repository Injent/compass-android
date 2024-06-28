package ru.bgitu.feature.about.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.bgitu.core.data.repository.CompassRepository

class AboutViewModel(
    private val compassRepository: CompassRepository,
    currentVersionCode: Long
) : ViewModel() {

    private val _changelog = MutableStateFlow<String?>(null)
    val changelog = _changelog.asStateFlow()

    init {
        viewModelScope.launch {
            compassRepository.getChangelog(currentVersionCode)
                .onSuccess { changelogMd ->
                    _changelog.update { changelogMd }
                }
        }
    }
}