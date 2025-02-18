package ru.bgitu.core.data.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.bgitu.core.data.downloader.AndroidFileDownloader
import ru.bgitu.core.data.repository.CompassRepository
import ru.bgitu.core.data.repository.FirstOfflineScheduleRepository
import ru.bgitu.core.data.repository.NotificationRepository
import ru.bgitu.core.data.repository.ScheduleRepository
import ru.bgitu.core.data.util.AndroidNetworkMonitor
import ru.bgitu.core.data.util.NetworkMonitor

val DataModuleMinified = module {
    singleOf(::FirstOfflineScheduleRepository) bind ScheduleRepository::class
    singleOf(::NotificationRepository)
}

val DataModule = module {
    includes(FlavoredDataModule)
    includes(DataModuleMinified)

    singleOf(::AndroidNetworkMonitor) bind NetworkMonitor::class

    singleOf(::CompassRepository)

    singleOf(::AndroidFileDownloader)
}
