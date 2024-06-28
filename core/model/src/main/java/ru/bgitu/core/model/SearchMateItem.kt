package ru.bgitu.core.model

data class SearchMateItem(
    val userId: Int,
    val fullName: String,
    val bio: String,
    val isVerified: Boolean,
    val contacts: Contacts?
)