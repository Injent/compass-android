package ru.bgitu.core.model

import kotlinx.serialization.Serializable

@Serializable
enum class UserRole {
    DEV,
    AWARDED,
    REGULAR,
    EXPERT
}