package ru.bgitu.core.database.di

import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import ru.bgitu.core.database.CompassDatabase

val DatabaseModule = module {
    single<CompassDatabase> {
        Room.databaseBuilder(
            androidContext(),
            CompassDatabase::class.java,
            "bgitu-database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    factory { get<CompassDatabase>().scheduleDao() }
    factory { get<CompassDatabase>().noteDao() }
}