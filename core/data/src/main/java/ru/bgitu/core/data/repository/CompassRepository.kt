package ru.bgitu.core.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.datetime.LocalDate
import ru.bgitu.core.common.Result
import ru.bgitu.core.common.map
import ru.bgitu.core.datastore.SettingsRepository
import ru.bgitu.core.model.ProfessorClass
import ru.bgitu.core.network.CompassService


class CompassRepository(
    private val compassService: CompassService,
    private val settings: SettingsRepository
) {
    suspend fun getChangelog(versionCode: Long): Result<String> {
        return compassService.getChangelog(versionCode).map(ByteArray::decodeToString)
    }

    suspend fun searchProfessor(query: String): Result<List<String>> {
        return compassService.searchProfessors(query)
    }

    suspend fun getProfessorSchedule(
        professorName: String,
        from: LocalDate,
        to: LocalDate
    ): Result<List<ProfessorClass>> {
        return compassService.getProfessorSchedule(professorName, from, to)
            .onSuccess {
                settings.updateMetadata {
                    it.copy(
                        recentProfessorSearch = it.recentProfessorSearch + professorName
                    )
                }
            }
    }

    fun getRecentProfessors(): Flow<List<String>> {
        return settings.metadata.mapLatest { it.recentProfessorSearch }
    }
}