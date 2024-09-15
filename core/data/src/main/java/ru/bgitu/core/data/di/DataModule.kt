package ru.bgitu.core.data.di

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.bgitu.core.common.di.CommonQualifiers
import ru.bgitu.core.data.downloader.AndroidFileDownloader
import ru.bgitu.core.data.repository.CompassRepository
import ru.bgitu.core.data.repository.FirstOfflineScheduleRepository
import ru.bgitu.core.data.repository.ScheduleRepository
import ru.bgitu.core.data.util.AndroidNetworkMonitor
import ru.bgitu.core.data.util.NetworkMonitor

val DataModule = module {
    single {
        AndroidNetworkMonitor(androidContext())
    } bind NetworkMonitor::class

    single<ScheduleRepository> {
        FirstOfflineScheduleRepository(
            serviceApi = get(),
            settingsRepository = get(),
            scheduleDao = get(),
        )
    }

    single {
        CompassRepository(
            compassService = get(),
            settings = get()
        )
    }

    single {
        AndroidFileDownloader(
            ioDispatcher = get(CommonQualifiers.DispatcherIO),
        )
    }
}
