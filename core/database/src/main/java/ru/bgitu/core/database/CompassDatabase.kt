package ru.bgitu.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.bgitu.core.database.dao.ScheduleDao
import ru.bgitu.core.database.dao.SubjectDao
import ru.bgitu.core.database.entity.LessonEntity
import ru.bgitu.core.database.entity.SubjectEntity
import ru.bgitu.core.database.util.LocalDateConverter
import ru.bgitu.core.database.util.LocalTimeConverter

@Database(
    version = 1,
    entities = [
        LessonEntity::class,
        SubjectEntity::class
    ]
)
@TypeConverters(
    LocalTimeConverter::class,
    LocalDateConverter::class
)
abstract class CompassDatabase : RoomDatabase() {
    abstract fun scheduleDao(): ScheduleDao
    abstract fun subjectDao(): SubjectDao

    companion object {
        const val ENTITY_LESSON = "Lesson"
        const val ENTITY_SUBJECT = "Subject"
    }
}