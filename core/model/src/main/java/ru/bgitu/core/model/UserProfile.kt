package ru.bgitu.core.model

data class UserProfile(
    val userId: Long,
    val bio: String = "",
    val avatarUrl: String?,
    val lastName: String = "",
    val firstName: String,
    val contacts: Contacts?,
    val variants: List<VariantEntry>,
    val publicProfile: Boolean,
) {

    data class VariantEntry(
        val subjectName: String,
        val variant: Int
    )
}