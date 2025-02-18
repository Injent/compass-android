package ru.bgitu.core.data.util

interface PushInterface {
    fun setNotificationDelegationEnabled(enabled: Boolean)

    suspend fun subscribeToTopic(topic: String)

    suspend fun unsubscribeFromTopic(topic: String)
}