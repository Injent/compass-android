package ru.bgitu.core.data.push

import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.tasks.await
import ru.bgitu.core.data.util.PushInterface

class FirebasePushInterface(
    private val messaging: FirebaseMessaging
) : PushInterface {
    override fun setNotificationDelegationEnabled(enabled: Boolean) {
        messaging.setNotificationDelegationEnabled(enabled)
    }

    override suspend fun subscribeToTopic(topic: String) {
        messaging.subscribeToTopic(topic).await()
    }

    override suspend fun unsubscribeFromTopic(topic: String) {
        messaging.unsubscribeFromTopic(topic).await()
    }
}