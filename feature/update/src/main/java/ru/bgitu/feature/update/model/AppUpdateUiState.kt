package ru.bgitu.feature.update.model

import ru.bgitu.core.common.TextResource

data class AppUpdateUiState(
    val downloadStarted: Boolean = false,
    val errorText: TextResource? = null
)
