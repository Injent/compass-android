package ru.bgitu.feature.professor_search.presentation.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus
import ru.bgitu.core.common.DateTimeUtil
import ru.bgitu.core.common.TextResource
import ru.bgitu.core.common.eventChannel
import ru.bgitu.core.common.getOrElse
import ru.bgitu.core.data.repository.CompassRepository
import ru.bgitu.core.data.util.NetworkMonitor
import ru.bgitu.core.datastore.SettingsRepository
import ru.bgitu.core.model.ProfessorClass

sealed class ProfessorDetailsIntent {
    data class ChangeFilter(val filterByDays: Boolean) : ProfessorDetailsIntent()
    data class ChangePage(val page: Int) : ProfessorDetailsIntent()
    data object Back : ProfessorDetailsIntent()
}

sealed class ProfessorDetailsEvent {
    data class ShowError(val errorDetails: TextResource) : ProfessorDetailsEvent()
    data object Back : ProfessorDetailsEvent()
}

sealed class FilteredSchedule {
    data class ByWeek(
        val selectedWeek: DayOfWeek? = null,
        val schedules: Map<LocalDate, List<ProfessorClass>> = emptyMap()
    ) : FilteredSchedule()

    data class All(
        val classes: List<ProfessorClass> = emptyList()
    ) : FilteredSchedule()
}

data class ProfessorDetailsUiState internal constructor(
    val fromDate: LocalDate = DateTimeUtil.currentDate,
    val toDate: LocalDate = DateTimeUtil.getEndOfWeek(fromDate.plus(2, DateTimeUnit.WEEK)),
    val dateBoundary: ClosedRange<LocalDate> = fromDate..toDate,
    val professorName: String,
    val schedules: List<ProfessorClass> = emptyList(),
    val selectedPage: Int,
    val filterByDays: Boolean = false,
    val loading: Boolean = true,
)

class TeacherDetailsViewModel(
    private val compassRepository: CompassRepository,
    private val settingsRepository: SettingsRepository,
    val teacherName: String,
    networkMonitor: NetworkMonitor
) : ViewModel() {
    private val _events = eventChannel<ProfessorDetailsEvent>()
    val events = _events.receiveAsFlow()

    private val _uiState = MutableStateFlow(
        ProfessorDetailsUiState(
            professorName = teacherName,
            selectedPage = DateTimeUtil.currentDate.dayOfWeek.ordinal
                .coerceAtMost(DayOfWeek.SATURDAY.ordinal)
        )
    )
    private val fullSchedule = networkMonitor.isOnline.map {
        var id = 0L
        compassRepository.getProfessorSchedule(
            professorName = teacherName,
            from = _uiState.value.fromDate,
            to = _uiState.value.toDate
        )
            .getOrElse {
                _events.trySend(ProfessorDetailsEvent.ShowError(it.details))
                emptyList()
            }
            .map {
                id++
                it.copy(id = id)
            }
    }

    val uiState = combine(
        fullSchedule,
        _uiState,
        settingsRepository.data,
    ) { fullSchedule, state, userdata ->
        state.copy(
            schedules = fullSchedule,
            loading = false,
            filterByDays = userdata.userPrefs.teacherFilterByDays
        )
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = _uiState.value
        )

    fun onIntent(intent: ProfessorDetailsIntent) {
        when (intent) {
            ProfessorDetailsIntent.Back -> {
                _events.trySend(ProfessorDetailsEvent.Back)
            }
            is ProfessorDetailsIntent.ChangeFilter -> {
                viewModelScope.launch {
                    settingsRepository.updateUserPrefs {
                        it.copy(teacherFilterByDays = intent.filterByDays)
                    }
                }
            }
            is ProfessorDetailsIntent.ChangePage -> {
                _uiState.update {
                    it.copy(
                        selectedPage = intent.page,
                        filterByDays = true
                    )
                }
                viewModelScope.launch {
                    settingsRepository.updateUserPrefs { it.copy(teacherFilterByDays = true) }
                }
            }
        }
    }
}