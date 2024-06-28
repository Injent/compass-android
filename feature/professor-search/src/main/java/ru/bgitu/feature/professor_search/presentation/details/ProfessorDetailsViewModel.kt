package ru.bgitu.feature.professor_search.presentation.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.updateAndGet
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
import ru.bgitu.core.datastore.SettingsRepository
import ru.bgitu.core.model.ProfessorClass

sealed class ProfessorDetailsIntent {
    data class ChangeDate(val date: LocalDate) : ProfessorDetailsIntent()
    data class ChangeWeek(val week: DayOfWeek) : ProfessorDetailsIntent()
    data class ChangeFilter(val sortByWeeks: Boolean) : ProfessorDetailsIntent()
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

    data class ByDays(
        val selectedDate: LocalDate? = null,
        val classes: List<ProfessorClass> = emptyList()
    ) : FilteredSchedule()
}

data class ProfessorDetailsUiState internal constructor(
    val fromDate: LocalDate = DateTimeUtil.currentDate,
    val toDate: LocalDate = DateTimeUtil.getEndOfWeek(fromDate.plus(2, DateTimeUnit.WEEK)),
    val dateBoundary: ClosedRange<LocalDate> = fromDate..toDate,
    val professorName: String,
    val filteredSchedule: FilteredSchedule = FilteredSchedule.ByWeek(),
) {
    val filteredByWeek: Boolean
        get() = filteredSchedule is FilteredSchedule.ByWeek
}

class ProfessorDetailsViewModel(
    private val compassRepository: CompassRepository,
    professorNameArg: String,
    private val settingsRepository: SettingsRepository
) : ViewModel() {
    private val _events = eventChannel<ProfessorDetailsEvent>()
    val events = _events.receiveAsFlow()

    val professorName = MutableStateFlow(professorNameArg)
    private val _uiState = MutableStateFlow(ProfessorDetailsUiState(professorName = professorName.value))
    private val fullSchedule = professorName.mapLatest { professor ->
        var id = 0L
        compassRepository.getProfessorSchedule(
            professorName = professor,
            from = _uiState.value.fromDate,
            to = _uiState.value.toDate
        )
            .getOrElse { emptyList() }
            .map {
                id++
                it.copy(id = id)
            }
    }
    private val selectedDate = MutableStateFlow<LocalDate?>(null)
    private val selectedWeek = MutableStateFlow(_uiState.value.fromDate.dayOfWeek)

    val uiState = combine(
        fullSchedule,
        settingsRepository.data,
        selectedDate,
        selectedWeek
    ) { fullSchedule, userdata, selectedDate, selectedWeek ->
        val filteredSchedule = if (userdata.userPrefs.teacherSortByWeeks) {
            FilteredSchedule.ByWeek(
                selectedWeek = selectedWeek,
                schedules = fullSchedule.filter { it.date.dayOfWeek == selectedWeek }
                    .groupBy { it.date }
            )
        } else {
            FilteredSchedule.ByDays(
                selectedDate = selectedDate,
                classes = if (selectedDate == null) {
                    fullSchedule
                } else {
                    fullSchedule.filter {
                        it.date == selectedDate
                    }
                }
            )
        }

        _uiState.updateAndGet {
            it.copy(filteredSchedule = filteredSchedule)
        }
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ProfessorDetailsUiState(professorName = professorNameArg)
        )

    fun changeProfessorName(name: String) {

    }

    fun onIntent(intent: ProfessorDetailsIntent) {
        when (intent) {
            is ProfessorDetailsIntent.ChangeDate -> {
                selectedDate.update { old ->
                    intent.date.takeIf { new -> new != old }
                }
            }
            ProfessorDetailsIntent.Back -> {
                _events.trySend(ProfessorDetailsEvent.Back)
            }
            is ProfessorDetailsIntent.ChangeFilter -> {
                viewModelScope.launch {
                    settingsRepository.updateUserPrefs {
                        it.copy(teacherSortByWeeks = intent.sortByWeeks)
                    }
                }
            }
            is ProfessorDetailsIntent.ChangeWeek -> {
                selectedWeek.value = intent.week
            }
        }
    }
}