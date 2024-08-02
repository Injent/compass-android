package ru.bgitu.app

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import com.vk.id.VKID
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

class BgituApp : Application(), KoinComponent, ImageLoaderFactory {
    override fun onCreate() {
        super.onCreate()
        VKID.init(this)
    }

    override fun newImageLoader(): ImageLoader = get()
}
