package ru.bgitu.app.di

import coil.ImageLoader
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.request.CachePolicy
import coil.util.DebugLogger
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.bgitu.app.presentation.MainViewModel
import ru.bgitu.components.signin.di.SignInModule
import ru.bgitu.components.sync.di.SyncModule
import ru.bgitu.components.updates.impl.di.UpdatesModule
import ru.bgitu.core.common.di.CommonModule
import ru.bgitu.core.data.di.DataModule
import ru.bgitu.core.database.di.DatabaseModule
import ru.bgitu.core.datastore.di.DatastoreModule
import ru.bgitu.core.domain.di.DomainModule
import ru.bgitu.core.network.di.NetworkModule
import ru.bgitu.core.notifications.di.NotificationsModule
import ru.bgitu.feature.about.di.AboutModule
import ru.bgitu.feature.findmate.di.FindMateModule
import ru.bgitu.feature.groups.di.GroupsModule
import ru.bgitu.feature.home.impl.di.HomeModule
import ru.bgitu.feature.login.di.LoginModule
import ru.bgitu.feature.notes.di.NotesModule
import ru.bgitu.feature.onboarding.di.OnboardingModule
import ru.bgitu.feature.professor_search.di.ProfessorSearchModule
import ru.bgitu.feature.profile.di.ProfileModule
import ru.bgitu.feature.profile_settings.di.ProfileSettingsModule
import ru.bgitu.feature.schedule_notifier.impl.di.ScheduleNotifierModule
import ru.bgitu.feature.schedule_widget.di.ScheduleWidgetModule
import ru.bgitu.feature.settings.di.SettingsModule
import ru.bgitu.feature.update.di.AppUpdateModule
import java.io.File

val AppModule = module {
    includes(
        // Core modules. Order matters!
        CommonModule,
        DatastoreModule,
        NetworkModule,
        DatabaseModule,
        DataModule,
        DomainModule,
        // Components modules
        SyncModule,
        UpdatesModule,
        SignInModule,
        // Feature modules
        HomeModule,
        LoginModule,
        ProfileModule,
        NotificationsModule,
        AppUpdateModule,
        SettingsModule,
        AboutModule,
        ProfileSettingsModule,
        ScheduleNotifierModule,
        ProfessorSearchModule,
        ScheduleWidgetModule,
        FindMateModule,
        GroupsModule,
        OnboardingModule,
        NotesModule,
    )

    viewModel {
        MainViewModel(
            appUpdateManager = get(),
            compassAuthenticator = get(),
            settingsRepository = get(),
            syncManager = get()
        )
    }

    factory {
        val context = androidContext()
        ImageLoader(context).newBuilder()
            .memoryCachePolicy(CachePolicy.ENABLED)
            .memoryCache {
                MemoryCache.Builder(context)
                    .maxSizePercent(percent = 0.1)
                    .strongReferencesEnabled(true)
                    .build()
            }
            .diskCachePolicy(CachePolicy.ENABLED)
            .diskCache {
                DiskCache.Builder()
                    .maxSizePercent(percent = 0.1)
                    .directory(File(context.cacheDir, "images"))
                    .build()
            }
            .logger(DebugLogger())
            .build()
    }
}
