package ru.bgitu.app

import android.app.Application
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.SingletonImageLoader
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class BgituApp : Application(), KoinComponent, SingletonImageLoader.Factory {
    override fun newImageLoader(context: PlatformContext) = inject<ImageLoader>().value
}
