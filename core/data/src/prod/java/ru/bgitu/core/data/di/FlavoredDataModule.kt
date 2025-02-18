package ru.bgitu.core.data.di

import com.google.firebase.messaging.FirebaseMessaging
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.bgitu.core.data.push.FirebasePushInterface
import ru.bgitu.core.data.util.PushInterface

internal val FlavoredDataModule = module {
    factory {
        FirebasePushInterface(
            FirebaseMessaging.getInstance()
        )
    } bind PushInterface::class
}