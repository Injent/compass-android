package ru.bgitu.core.database.di

import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import ru.bgitu.core.database.CompassDatabase
import ru.bgitu.core.database.dao.ScheduleDao

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

    factory<ScheduleDao> {
        get<CompassDatabase>().scheduleDao()
    }
}