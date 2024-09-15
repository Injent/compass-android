package ru.bgitu.core.data.repository

import android.os.Build
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.datetime.LocalDate
import ru.bgitu.core.common.Result
import ru.bgitu.core.common.map
import ru.bgitu.core.datastore.SettingsRepository
import ru.bgitu.core.model.ProfessorClass
import ru.bgitu.core.model.SearchMateItem
import ru.bgitu.core.model.UserProfile
import ru.bgitu.core.network.CompassService
import ru.bgitu.core.network.model.NetworkSearchMateItem
import ru.bgitu.core.network.model.NetworkUserProfile
import ru.bgitu.core.network.model.toExternalModel
import ru.bgitu.core.network.model.toNetworkModel
import java.util.Locale


class CompassRepository(
    private val compassService: CompassService,
    private val settings: SettingsRepository
) {
    suspend fun getUserProfile(userId: Long) = compassService.getUserProfile(userId)
        .map(NetworkUserProfile::toExternalModel)

    suspend fun updateUserProfile(profile: UserProfile) =
        compassService.updateUserProfile(profile.toNetworkModel())

    suspend fun getChangelog(versionCode: Long): Result<String> {
        return compassService.getChangelog(versionCode).map(ByteArray::decodeToString)
    }

    suspend fun searchProfessor(query: String): Result<List<String>> {
        return compassService.searchProfessors(query)
    }

    suspend fun searchMates(subjectName: String): Result<List<SearchMateItem>> {
        return compassService.searchMates(subjectName, 1).map(NetworkSearchMateItem::toExternalModel)
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

    suspend fun searchGroups(query: String) = compassService.searchGroups(query)

    private fun getDeviceName(): String {
        fun String.localCapitalize(): String {
            return this.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString()
            }
        }
        val manufacturer = Build.MANUFACTURER
        val model = Build.MODEL
        return if (model.startsWith(manufacturer)) {
            model.localCapitalize()
        } else manufacturer.localCapitalize() + " " + model
    }
}