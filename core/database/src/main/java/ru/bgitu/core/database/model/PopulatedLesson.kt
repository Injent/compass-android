package ru.bgitu.core.database.model

import androidx.room.Embedded
import androidx.room.Relation
import ru.bgitu.core.database.entity.LessonEntity
import ru.bgitu.core.database.entity.SubjectEntity
import ru.bgitu.core.model.Lesson
import ru.bgitu.core.model.Subject

data class PopulatedLesson(
    @Embedded
    val lesson: LessonEntity,
    @Relation(
        entity = SubjectEntity::class,
        parentColumn = LessonEntity.Subject,
        entityColumn = SubjectEntity.SubjectId,
    )
    val subject: SubjectEntity
) {
    fun toExternalModel() = with(lesson) {
        Lesson(
            lessonId = id,
            building = building,
            date = date,
            subject = Subject(
                subjectId = this@PopulatedLesson.subject.id,
                name = this@PopulatedLesson.subject.name
            ),
            startAt = startAt,
            endAt = endAt,
            classroom = classroom,
            teacher = teacher,
            isLecture = isLecture
        )
    }
}
