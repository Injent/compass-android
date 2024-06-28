package ru.bgitu.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import ru.bgitu.core.database.CompassDatabase.Companion.ENTITY_LESSON

@Entity(tableName = ENTITY_LESSON)
data class LessonEntity(
    @PrimaryKey @ColumnInfo(name = LessonId) val id: Int,
    @ColumnInfo(name = Building) val building: String,
    @ColumnInfo(name = Subject) val subject: Int,
    @ColumnInfo(name = Date) val date: LocalDate,
    @ColumnInfo(name = StartAt) val startAt: LocalTime,
    @ColumnInfo(name = EndAt) val endAt: LocalTime,
    @ColumnInfo(name = Classroom) val classroom: String,
    @ColumnInfo(name = Teacher) val teacher: String,
    @ColumnInfo(name = IsLecture) val isLecture: Boolean
) {
    companion object {
        const val LessonId = "lesson_id"
        const val Building = "building"
        const val Date = "date"
        const val StartAt = "start_at"
        const val EndAt = "end_at"
        const val Subject = "subject"
        const val Classroom = "classroom"
        const val Teacher = "teacher"
        const val IsLecture = "is_lecture"
    }
}
