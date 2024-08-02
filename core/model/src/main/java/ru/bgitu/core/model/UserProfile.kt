package ru.bgitu.core.model

data class UserProfile(
    val userId: Long,
    val bio: String = "",
    val avatarUrl: String?,
    val displayName: String,
    val contacts: Contacts?,
    val variants: List<VariantEntry>,
    val userRole: UserRole,
    val publicProfile: Boolean,
) {

    data class VariantEntry(
        val subjectName: String,
        val variant: Int
    )
}