package ru.bgitu.core.network.model

import kotlinx.serialization.Serializable
import ru.bgitu.core.model.SearchMateItem

@Serializable
data class NetworkSearchMateItem(
    val userId: Int,
    val fullName: String,
    val bio: String,
    val isVerified: Boolean,
    val contacts: NetworkContacts
)

fun NetworkSearchMateItem.toExternalModel() = SearchMateItem(
    userId = userId,
    fullName = fullName,
    bio = bio,
    isVerified = isVerified,
    contacts = contacts.toExternalModel()
)