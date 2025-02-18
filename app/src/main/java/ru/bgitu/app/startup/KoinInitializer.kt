package ru.bgitu.app.startup

import android.content.Context
import androidx.startup.Initializer
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext
import ru.bgitu.app.di.MinifiedAppModule

@Suppress("unused")
class KoinInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        GlobalContext.startKoin {
            allowOverride(true)
            androidContext(context)
            modules(MinifiedAppModule)
        }
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return mutableListOf()
    }
}
