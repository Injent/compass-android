package ru.bgitu.app

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

class BgituApp : Application(), KoinComponent, ImageLoaderFactory {

    override fun newImageLoader(): ImageLoader = get()
}
