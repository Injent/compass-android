package ru.bgitu.core.data.notifications

import com.huawei.hms.push.HmsMessageService
import com.huawei.hms.push.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get
import ru.bgitu.core.model.settings.SubscribedTopic
import ru.bgitu.core.notifications.Notifier
import kotlin.random.Random

class HuaweiPushService : HmsMessageService() {
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        get<Notifier>().showPushNotification(
            notificationId = Random.nextInt(),
            title = message.dataOfMap["title"] ?: return,
            body = message.dataOfMap["body"] ?: return,
            url = message.dataOfMap["url"],
            topic = runCatching {
                SubscribedTopic.valueOf(message.dataOfMap["notificationType"] ?: return)
            }.getOrElse { return }
        )
    }

    override fun onNewToken(token: String?) {
        super.onNewToken(token)
    }
}