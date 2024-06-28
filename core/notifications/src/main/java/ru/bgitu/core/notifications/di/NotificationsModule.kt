package ru.bgitu.core.notifications.di

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.bgitu.core.notifications.Notifier
import ru.bgitu.core.notifications.SystemTrayNotifier
import ru.bgitu.core.notifications.channels.AppChannelManager

val NotificationsModule = module {
    includes(flavoredNotificationsModule)

    factory {
        AppChannelManager(androidContext())
    }

    single {
        SystemTrayNotifier(
            context = androidContext()
        )
    } bind Notifier::class
}