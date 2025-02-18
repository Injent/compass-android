package ru.bgitu.core.data.notifications

import com.huawei.hms.push.HmsMessaging
import ru.bgitu.core.data.util.PushInterface
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class HmsPushInterface(
    private val messaging: HmsMessaging
) : PushInterface {
    override fun setNotificationDelegationEnabled(enabled: Boolean) {
        if (enabled) {
            messaging.turnOnPush()
        } else {
            messaging.turnOffPush()
        }
            .addOnCompleteListener {
                if (!it.isSuccessful) {
                    throw it.exception
                }
            }
    }

    override suspend fun subscribeToTopic(topic: String) = suspendCoroutine { continuation ->
        messaging.subscribe(topic)
            .addOnCompleteListener {
                continuation.resume(Unit)
                if (!it.isSuccessful) {
                    throw it.exception
                }
            }
    }

    override suspend fun unsubscribeFromTopic(topic: String) = suspendCoroutine { continuation ->
        messaging.unsubscribe(topic)
            .addOnCompleteListener {
                continuation.resume(Unit)
                if (!it.isSuccessful) {
                    throw it.exception
                }
            }
    }
}