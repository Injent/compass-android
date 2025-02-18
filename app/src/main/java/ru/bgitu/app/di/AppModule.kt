package ru.bgitu.app.di

import coil3.ImageLoader
import coil3.disk.DiskCache
import coil3.memory.MemoryCache
import coil3.network.ktor3.KtorNetworkFetcherFactory
import coil3.request.CachePolicy
import coil3.util.DebugLogger
import io.ktor.client.HttpClient
import okio.Path.Companion.toOkioPath
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import ru.bgitu.app.BuildConfig
import ru.bgitu.app.presentation.MainViewModel
import ru.bgitu.components.sync.di.SyncModule
import ru.bgitu.components.updates.impl.di.UpdatesModule
import ru.bgitu.core.common.di.CommonModule
import ru.bgitu.core.data.di.DataModule
import ru.bgitu.core.data.di.DataModuleMinified
import ru.bgitu.core.datastore.di.DatastoreModule
import ru.bgitu.core.domain.di.DomainModule
import ru.bgitu.core.network.di.NetworkModule
import ru.bgitu.core.notifications.di.NotificationsModule
import ru.bgitu.feature.about.di.AboutModule
import ru.bgitu.feature.groups.di.GroupsModule
import ru.bgitu.feature.home.impl.di.HomeModule
import ru.bgitu.feature.professor_search.di.ProfessorSearchModule
import ru.bgitu.feature.profile.di.ProfileModule
import ru.bgitu.feature.schedule_notifier.impl.di.ScheduleNotifierModule
import ru.bgitu.feature.schedule_widget.di.ScheduleWidgetModule
import ru.bgitu.feature.settings.di.SettingsModule
import ru.bgitu.feature.update.di.AppUpdateModule
import java.io.File

/**
 * Used for widget app start and fast response
 */
val MinifiedAppModule = module {
    includes(
        CommonModule,
        DatastoreModule,
        NetworkModule,
        NotificationsModule,
        DataModuleMinified,
        SyncModule,
        ScheduleNotifierModule
    )
}

val AppModule = module {
    includes(
        // Core modules. Order matters!
        CommonModule,
        DatastoreModule,
        NetworkModule,
        NotificationsModule,
        DataModule,
        DomainModule,
        // Components modules
        SyncModule,
        UpdatesModule,
        // Feature modules
        HomeModule,
        ProfileModule,
        AppUpdateModule,
        SettingsModule,
        AboutModule,
        ScheduleNotifierModule,
        ProfessorSearchModule,
        ScheduleWidgetModule,
        GroupsModule
    )

    viewModelOf(::MainViewModel)

    factory {
        val context = androidContext()
        ImageLoader(context).newBuilder()
            .memoryCachePolicy(CachePolicy.ENABLED)
            .memoryCache {
                MemoryCache.Builder()
                    .maxSizePercent(context = context, percent = 0.1)
                    .strongReferencesEnabled(true)
                    .build()
            }
            .diskCachePolicy(CachePolicy.ENABLED)
            .diskCache {
                DiskCache.Builder()
                    .maxSizePercent(percent = 0.1)
                    .directory(File(context.cacheDir, "images").toOkioPath())
                    .build()
            }
            .apply {
                if (BuildConfig.DEBUG) {
                    logger(DebugLogger())
                }
            }
            .components {
                add(KtorNetworkFetcherFactory(get<HttpClient>()))
            }
            .build()
    }
}
