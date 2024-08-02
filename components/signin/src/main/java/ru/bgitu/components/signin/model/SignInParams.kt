package ru.bgitu.components.signin.model

data class SignInParams(
    val authMethod: AuthMethod,
    var idToken: String,
    var fullName: String,
    var avatarUrl: String?,
)