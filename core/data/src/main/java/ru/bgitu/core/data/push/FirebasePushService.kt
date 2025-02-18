package ru.bgitu.core.data.push

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.koin.android.ext.android.get
import org.koin.core.component.KoinComponent
import ru.bgitu.core.model.settings.SubscribedTopic
import ru.bgitu.core.notifications.Notifier
import kotlin.random.Random

class FirebasePushService : FirebaseMessagingService(), KoinComponent {

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        get<Notifier>().showPushNotification(
            notificationId = Random.nextInt(),
            title = message.data["title"] ?: return,
            body = message.data["body"] ?: return,
            url = message.data["url"],
            topic = runCatching {
                SubscribedTopic.valueOf(message.data["notificationType"] ?: return)
            }.getOrElse { return }
        )
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }
}