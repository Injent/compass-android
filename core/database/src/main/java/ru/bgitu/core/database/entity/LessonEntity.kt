package ru.bgitu.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import ru.bgitu.core.model.Lesson
import ru.bgitu.core.model.Subject

@Entity(tableName = "lessons")
data class LessonEntity(
    @PrimaryKey @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "building") val building: String,
    @ColumnInfo(name = "subject_id") val subjectId: Int,
    @ColumnInfo(name = "subject") val subjectName: String,
    @ColumnInfo(name = "date") val date: LocalDate,
    @ColumnInfo(name = "start_at") val startAt: LocalTime,
    @ColumnInfo(name = "end_at") val endAt: LocalTime,
    @ColumnInfo(name = "classroom") val classroom: String,
    @ColumnInfo(name = "teacher") val teacher: String,
    @ColumnInfo(name = "is_lecture") val isLecture: Boolean
)