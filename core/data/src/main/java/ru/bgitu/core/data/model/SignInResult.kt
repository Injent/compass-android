package ru.bgitu.core.data.model

import ru.bgitu.core.common.TextResource

sealed class SignInResult private constructor() {
    data object Success : SignInResult()
    data class SuccessButGroupNotFound(val searchQueryForGroup: String) : SignInResult()
    data class Error(val details: TextResource) : SignInResult()
}