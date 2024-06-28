package ru.bgitu.core.data.di

import android.annotation.SuppressLint
import android.provider.Settings.Secure
import android.provider.Settings.Secure.ANDROID_ID
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.bgitu.core.common.di.CommonQualifiers
import ru.bgitu.core.data.repository.CompassAuthenticator
import ru.bgitu.core.data.repository.DefaultCompassAuthenticator

@SuppressLint("HardwareIds")
val flavoredDataModule = module {
    single<CompassAuthenticator> {
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