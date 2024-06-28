package ru.bgitu.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.bgitu.core.database.CompassDatabase.Companion.ENTITY_SUBJECT

@Entity(tableName = ENTITY_SUBJECT)
data class SubjectEntity(
    @PrimaryKey @ColumnInfo(name = SubjectId) val id: Int,
    @ColumnInfo(name = Name) val name: String
) {
    companion object {
        const val SubjectId = "subject_id"
        const val Name = "name"
    }
}
