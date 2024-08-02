package ru.bgitu.core.model

import kotlinx.serialization.Serializable

@Serializable
enum class UserRole {
    ADMIN,
    SPECIAL,
    REGULAR,
    POPULAR
}