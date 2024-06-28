package ru.bgitu.app.startup

import android.content.Context
import androidx.startup.Initializer
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.context.GlobalContext
import ru.bgitu.app.di.AppModule

class KoinInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        GlobalContext.startKoin {
            androidContext(context)
            modules(AppModule)

            workManagerFactory()
        }
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return mutableListOf()
    }
}
