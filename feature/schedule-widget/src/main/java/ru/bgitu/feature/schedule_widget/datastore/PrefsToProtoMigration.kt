package ru.bgitu.feature.schedule_widget.datastore

import androidx.datastore.core.DataMigration
import kotlinx.datetime.LocalDate
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import ru.bgitu.core.model.Lesson
import ru.bgitu.feature.schedule_widget.WidgetPb.WidgetDataPb
import ru.bgitu.feature.schedule_widget.WidgetPb.WidgetLessonPb
import ru.bgitu.feature.schedule_widget.copy
import ru.bgitu.feature.schedule_widget.model.ScheduleWidgetState
import ru.bgitu.feature.schedule_widget.model.WidgetOptions
import ru.bgitu.feature.schedule_widget.model.toProtoModel
import java.io.File

internal class PrefsToProtoMigration(
    private val oldPrefsFile: File
) : DataMigration<WidgetDataPb> {

    override suspend fun cleanUp() {
        oldPrefsFile.delete()
    }

    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun migrate(currentData: WidgetDataPb): WidgetDataPb {
        val json = Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
        }
        runCatching {
            val oldData = json.decodeFromStream<OldWidgetState>(oldPrefsFile.inputStream())
            return currentData.copy {
                this.options = oldData.options.toProtoModel()
                this.queryDate = oldData.queryDate.toString()
                this.lessons.addAll(oldData.classes.map { it.toWidgetLesson() })
            }
        }
        return ScheduleWidgetState.defaultProtoInstance()
    }

    override suspend fun shouldMigrate(currentData: WidgetDataPb): Boolean {
        return oldPrefsFile.exists()
    }

    @Serializable
    internal data class OldWidgetState(
        val queryDate: LocalDate,
        val options: WidgetOptions,
        val classes: List<Lesson>
    )

    private fun Lesson.toWidgetLesson(): WidgetLessonPb {
        return WidgetLessonPb.newBuilder()
            .setDate(date.toEpochDays())
            .setStartAt(startAt.toSecondOfDay())
            .setEndAt(endAt.toSecondOfDay())
            .setIsLecture(isLecture)
            .setBuilding(building)
            .setClassroom(classroom)
            .setSubject(subject.name)
            .build()
    }
}