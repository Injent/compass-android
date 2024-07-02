package ru.bgitu.core.data.di

import android.annotation.SuppressLint
import android.provider.Settings.Secure
import android.provider.Settings.Secure.ANDROID_ID
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.bgitu.core.common.di.CommonQualifiers
import ru.bgitu.core.data.repository.CompassAuthenticator
import ru.bgitu.core.data.repository.CompassRepository
import ru.bgitu.core.data.repository.DefaultCompassAuthenticator
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
            network = get(),
            settings = get()
        )
    }

    single {
        DefaultCompassAuthenticator(
            settings = get(),
            serviceApi = get(),
            networkMonitor = get(),
            database = get(),
            ioDispatcher = get(CommonQualifiers.DispatcherIO),
            deviceId = Secure.getString(androidContext().contentResolver, ANDROID_ID)
        )
    } bind CompassAuthenticator::class
}
