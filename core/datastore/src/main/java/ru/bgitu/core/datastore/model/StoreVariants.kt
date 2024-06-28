package ru.bgitu.core.datastore.model

import kotlinx.serialization.Serializable
import ru.bgitu.core.model.UserProfile

@Serializable
data class StoreVariants(
    val subjectName: String,
    val variant: Int
)

fun UserProfile.VariantEntry.toDataStoreModel() = StoreVariants(
    subjectName, variant
)

fun StoreVariants.toExternalModel() = UserProfile.VariantEntry(
    subjectName, variant
)