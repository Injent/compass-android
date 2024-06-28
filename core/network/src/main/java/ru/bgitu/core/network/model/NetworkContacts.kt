package ru.bgitu.core.network.model

import kotlinx.serialization.Serializable
import ru.bgitu.core.model.Contacts

@Serializable
data class NetworkContacts(
    val tg: String?,
    val vk: String?
)

fun NetworkContacts.toExternalModel() = Contacts(
    tg = tg,
    vk = vk
)

fun Contacts.toNetworkModel() = NetworkContacts(
    tg = tg,
    vk = vk
)