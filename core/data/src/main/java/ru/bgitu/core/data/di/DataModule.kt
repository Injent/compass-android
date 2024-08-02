package ru.bgitu.core.data.di

import android.annotation.SuppressLint
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.bgitu.core.data.repository.CompassRepository
import ru.bgitu.core.data.repository.FirstOfflineScheduleRepository
import ru.bgitu.core.data.repository.ScheduleRepository
import ru.bgitu.core.data.util.AndroidNetworkMonitor
import ru.bgitu.core.data.util.NetworkMonitor

@SuppressLint("HardwareIds")
val DataModule = module {
    includes(FlavoredDataModule)

    single {
        AndroidNetworkMonitor(androidContext())
    } bind NetworkMonitor::class

    single<ScheduleRepository> {
        FirstOfflineScheduleRepository(
            serviceApi = get(),
            settings = get(),
            scheduleDao = get(),
            subjectDao = get(),
        )
    }

    single {
        CompassRepository(
            compassService = get(),
            settings = get()
        )
    }
}
