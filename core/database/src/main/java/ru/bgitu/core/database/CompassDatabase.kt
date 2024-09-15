package ru.bgitu.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.bgitu.core.database.dao.ScheduleDao
import ru.bgitu.core.database.entity.LessonEntity
import ru.bgitu.core.database.util.LocalDateConverter
import ru.bgitu.core.database.util.LocalTimeConverter

@Database(
    version = 2,
    entities = [
        LessonEntity::class,
    ]
)
@TypeConverters(
    LocalTimeConverter::class,
    LocalDateConverter::class
)
abstract class CompassDatabase : RoomDatabase() {
    abstract fun scheduleDao(): ScheduleDao
}