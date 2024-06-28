package ru.bgitu.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.bgitu.core.model.UserProfile

@Serializable
class NetworkUserProfile(
    @SerialName("id")
    val userId: Long,
    val bio: String,
    val avatarUrl: String?,
    val firstName: String,
    val lastName: String,
    val contacts: NetworkContacts?,
    val variants: List<NetworkVariantEntry>,
    val publicProfile: Boolean
) {
    @Serializable
    data class NetworkVariantEntry(
        val subjectName: String,
        val variant: Int
    )
}

fun NetworkUserProfile.toExternalModel() = UserProfile(
    userId = userId,
    bio = bio,
    avatarUrl = avatarUrl,
    firstName = firstName,
    lastName = firstName,
    contacts = contacts?.toExternalModel(),
    publicProfile = publicProfile,
    variants = variants.map { entry ->
        UserProfile.VariantEntry(
            subjectName = entry.subjectName,
            variant = entry.variant
        )
    }
)

fun UserProfile.toNetworkModel() = NetworkUserProfile(
    userId = userId,
    bio = bio,
    avatarUrl = avatarUrl,
    firstName = firstName,
    lastName = lastName,
    contacts = contacts?.toNetworkModel(),
    variants = variants.map { entry ->
        NetworkUserProfile.NetworkVariantEntry(
            subjectName = entry.subjectName,
            variant = entry.variant
        )
    },
    publicProfile = publicProfile
)