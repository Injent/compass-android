package ru.bgitu.core.data.testdoubles

import androidx.room.DatabaseConfiguration
import androidx.room.InvalidationTracker
import androidx.sqlite.db.SupportSQLiteOpenHelper
import ru.bgitu.core.database.CompassDatabase
import ru.bgitu.core.database.dao.ScheduleDao
import ru.bgitu.core.database.dao.SubjectDao

class TestCompassDatabase : CompassDatabase() {
    override fun clearAllTables() {

    }

    override fun scheduleDao(): ScheduleDao {
        TODO()
    }

    override fun subjectDao(): SubjectDao {
        TODO()
    }

    override fun createInvalidationTracker(): InvalidationTracker {
       return InvalidationTracker(this)
    }

    override fun createOpenHelper(config: DatabaseConfiguration): SupportSQLiteOpenHelper {
        TODO()
    }
}

