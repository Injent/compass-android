package ru.bgitu.core.notifications.di

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.bgitu.core.notifications.Notifier
import ru.bgitu.core.notifications.SystemTrayNotifier

val flavoredNotificationsModule = module {
    single { SystemTrayNotifier(androidContext()) } bind Notifier::class
}