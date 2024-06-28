package ru.bgitu.core.data.model

import ru.bgitu.core.database.entity.SubjectEntity
import ru.bgitu.core.network.model.NetworkSubject

fun NetworkSubject.asEntity() = SubjectEntity(
    id = subjectId,
    name = name
)