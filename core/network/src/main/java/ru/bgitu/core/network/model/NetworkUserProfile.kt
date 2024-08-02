package ru.bgitu.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.bgitu.core.model.UserProfile
import ru.bgitu.core.model.UserRole

@Serializable
class NetworkUserProfile(
    @SerialName("id")
    val userId: Long,
    val bio: String,
    val avatarUrl: String?,
    val fullName: String,
    val contacts: NetworkContacts?,
    val variants: List<NetworkVariantEntry>,
    val role: UserRole,
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
    displayName = fullName,
    contacts = contacts?.toExternalModel(),
    publicProfile = publicProfile,
    userRole = role,
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
    fullName = displayName,
    contacts = contacts?.toNetworkModel(),
    variants = variants.map { entry ->
        NetworkUserProfile.NetworkVariantEntry(
            subjectName = entry.subjectName,
            variant = entry.variant
        )
    },
    role = userRole,
    publicProfile = publicProfile
)