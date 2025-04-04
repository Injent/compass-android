package ru.bgitu.core.datastore.model

import kotlinx.serialization.Serializable
import ru.bgitu.core.model.UserRole

@Serializable
internal data class DeprecatedStudentInfo(
    val studentId: Int,
    val groupId: Int,
    val groupName: String,
    val role: UserRole,
    val avatarUrl: String? = "",
    val name: String,
    val surname: String,
    val middleName: String?,
)