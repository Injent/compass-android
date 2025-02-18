package ru.bgitu.core.data.repository

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import ru.bgitu.core.common.Result
import ru.bgitu.core.common.runResulting
import ru.bgitu.core.data.util.PushInterface
import ru.bgitu.core.datastore.SettingsRepository
import ru.bgitu.core.model.settings.SubscribedTopic

class NotificationRepository(
    private val settingsRepository: SettingsRepository,
    private val messaging: PushInterface
) {
    private val mutex = Mutex()

    suspend fun init() = coroutineScope {
        if (settingsRepository.metadata.first().isPushMessagesInitialized) return@coroutineScope

        val subscribed = arrayOf(
            async { asyncSubscribeToTopic(SubscribedTopic.SEVERE) },
            async { asyncSubscribeToTopic(SubscribedTopic.SCHEDULE_CHANGE) },
            async { asyncSubscribeToTopic(SubscribedTopic.BROADCAST) },
        ).all { it.await().isSuccess }

        settingsRepository.updateMetadata { it.copy(isPushMessagesInitialized = subscribed) }
    }

    suspend fun subscribeToTopic(topic: SubscribedTopic): Result<Unit> = mutex.withLock {
        runResulting {
            messaging.subscribeToTopic(topic.name)
            settingsRepository.updateUserPrefs {
                it.copy(subscribedTopics = it.subscribedTopics + topic)
            }
        }
    }

    suspend fun unsubscribeFromTopic(topic: SubscribedTopic): Result<Unit> = mutex.withLock {
        return runResulting {
            messaging.unsubscribeFromTopic(topic.name)
            settingsRepository.updateUserPrefs {
                it.copy(subscribedTopics = it.subscribedTopics - topic)
            }
        }
    }

    suspend fun setNotificationDelegationEnabled(enabled: Boolean) = mutex.withLock(enabled) {
        runResulting {
            messaging.setNotificationDelegationEnabled(enabled)
            settingsRepository.updateUserPrefs {
                it.copy(notificationDelegationEnabled = enabled)
            }
        }
    }

    private suspend fun asyncSubscribeToTopic(topic: SubscribedTopic) = runResulting {
        messaging.subscribeToTopic(topic.name)
        settingsRepository.updateUserPrefs {
            it.copy(subscribedTopics = it.subscribedTopics + topic)
        }
    }
}