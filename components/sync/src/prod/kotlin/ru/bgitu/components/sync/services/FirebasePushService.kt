package ru.bgitu.components.sync.services

import com.google.firebase.messaging.FirebaseMessagingService
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.bgitu.core.data.model.CloudMessagingTokenType
import ru.bgitu.core.data.util.SyncManager

class FirebasePushService : FirebaseMessagingService(), KoinComponent {
    private val syncManager: SyncManager by inject()

    override fun onNewToken(token: String) {
        syncManager.refreshServicesToken(token, CloudMessagingTokenType.GMS)
    }
}