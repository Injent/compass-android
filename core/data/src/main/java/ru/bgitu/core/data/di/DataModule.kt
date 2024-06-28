package ru.bgitu.core.data.di

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.bgitu.core.common.di.CommonQualifiers
import ru.bgitu.core.data.repository.CompassRepository
import ru.bgitu.core.data.repository.DefaultHeadmanRepository
import ru.bgitu.core.data.repository.FirstOfflineScheduleRepository
import ru.bgitu.core.data.repository.HeadmanRepository
import ru.bgitu.core.data.repository.ScheduleRepository
import ru.bgitu.core.data.util.AndroidNetworkMonitor
import ru.bgitu.core.data.util.NetworkMonitor

val DataModule = module {
    includes(flavoredDataModule)

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

    single<HeadmanRepository> {
        DefaultHeadmanRepository(
            compassService = get(),
            settings = get(),
            ioDispatcher = get(CommonQualifiers.DispatcherIO)
        )
    }

    single {
        CompassRepository(
            network = get(),
            settings = get()
        )
    }
}
