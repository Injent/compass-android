package ru.bgitu.feature.schedule_notifier.impl.di

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import ru.bgitu.feature.schedule_notifier.api.ScheduleNotifier
import ru.bgitu.feature.schedule_notifier.impl.ScheduleNotifierAlarm

val ScheduleNotifierModule = module {
    single<ScheduleNotifier> {
        ScheduleNotifierAlarm(
            context = androidContext(),
            settingsRepository = get()
        )
    }
}