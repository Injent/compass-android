package ru.bgitu.components.signin.model

import ru.bgitu.core.common.TextResource

sealed interface SignInResult {
    data object Success : SignInResult
    data class Error(val errorMsg: TextResource) : SignInResult
}