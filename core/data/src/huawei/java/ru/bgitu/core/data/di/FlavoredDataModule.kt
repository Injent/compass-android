package ru.bgitu.core.data.di

import com.huawei.hms.push.HmsMessaging
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.bgitu.core.data.notifications.HmsPushInterface
import ru.bgitu.core.data.util.PushInterface

val FlavoredDataModule = module {
    factory {
        HmsPushInterface(
            HmsMessaging.getInstance(androidContext())
        )
    } bind PushInterface::class
}