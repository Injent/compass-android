package ru.bgitu.feature.home.impl.presentation

import android.content.Context
import android.icu.text.RelativeDateTimeFormatter
import androidx.compose.ui.util.fastAny
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.atDate
import kotlinx.datetime.plus
import ru.bgitu.core.common.DateTimeUtil
import ru.bgitu.core.common.TextResource
import ru.bgitu.core.common.eventChannel
import ru.bgitu.core.common.eventbus.EventBus
import ru.bgitu.core.common.eventbus.GlobalAppEvent
import ru.bgitu.core.data.model.ScheduleLoadState
import ru.bgitu.core.data.repository.ScheduleRepository
import ru.bgitu.core.data.util.SyncManager
import ru.bgitu.core.data.util.SyncStatus
import ru.bgitu.core.datastore.SettingsRepository
import ru.bgitu.core.model.Group
import ru.bgitu.core.model.Lesson
import ru.bgitu.core.ui.schedule.DayOfWeekSelectorUiState
import ru.bgitu.feature.home.BuildConfig
import ru.bgitu.feature.home.R
import java.time.DayOfWeek
import java.time.temporal.ChronoUnit
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

private const val SELECTED_DATE = "selected_date"

sealed interface HomeIntent {
    data class ChangeDate(val date: LocalDate) : HomeIntent
    data class ChangeGroupView(val group: Group) : HomeIntent
    data object NavigateToGroupSettings : HomeIntent
    data object NavigateToNewFeatures : HomeIntent
}

sealed interface HomeEvent {
    data object NavigateToGroupSettings : HomeEvent
    data object NavigateToNewFeatures : HomeEvent
}

sealed interface HomeUiState {
    data class Success(
        val selectedGroup: Group? = null,
        val savedGroups: List<Group> = emptyList(),
        val showGroups: Boolean,
        val isSyncing: Boolean = false
    ) : HomeUiState
    data object GroupNotSelected : HomeUiState
    data object Loading : HomeUiState
}

class HomeViewModel(
    private val savedStateHandle: SavedStateHandle,
    openScheduleDate: LocalDate?,
    private val settings: SettingsRepository,
    scheduleRepository: ScheduleRepository,
    syncManager: SyncManager,
    context: Context,
) : ViewModel() {

    private val _events = eventChannel<HomeEvent>()
    val events = _events.receiveAsFlow()

    private val _selectedGroup = MutableStateFlow(
        runBlocking { settings.data.first().primaryGroup }
    )
    init {
        viewModelScope.launch {
            EventBus.subscribe<GlobalAppEvent.ChangeGroup> {
                _selectedGroup.value = settings.data.first().primaryGroup
            }
        }
    }

    val uiState = combine(
        _selectedGroup,
        settings.data,
        syncManager.syncState,
    ) { selectedGroup, userdata, syncStatus ->
        if (userdata.primaryGroup == null) {
            return@combine HomeUiState.GroupNotSelected
        }
        HomeUiState.Success(
            selectedGroup = selectedGroup,
            savedGroups = listOf(userdata.primaryGroup!!) + userdata.userPrefs.savedGroups,
            showGroups = userdata.userPrefs.showGroupsOnMainScreen,
            isSyncing = syncStatus == SyncStatus.RUNNUNG
        )
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = HomeUiState.Loading
        )

    private val _selectedDate = savedStateHandle.getStateFlow(
        SELECTED_DATE, (openScheduleDate ?: getActualDate()).toEpochDays()
    )
        .map { LocalDate.fromEpochDays(it) }

    val scheduleState: StateFlow<ScheduleLoadState> = combine(
        _selectedDate,
        _selectedGroup,
        settings.data
    ) { selectedDate, selectedGroup, userdata ->
        when {
            selectedGroup == userdata.primaryGroup -> {
                scheduleRepository.getClassesStream(getActualDate(), 2).first()
            }
            selectedGroup != null -> {
                scheduleRepository.getNetworkClasses(
                    groupId = selectedGroup.id,
                    from = selectedDate,
                    to = selectedDate
                )
            }
            else -> ScheduleLoadState.Error(null)
        }
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ScheduleLoadState.Loading
        )

    val classLabelMessage: StateFlow<TextResource?> = combine(
        scheduleState,
        DateTimeUtil.currentDateTimeFlow(context),
    ) { scheduleLoad, time ->
        if (scheduleLoad is ScheduleLoadState.Success) {
            getCurrentClassLabelState(time, scheduleLoad[time.date])
        } else {
            null
        }
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null
        )

    val selectorUiState = combine(
        _selectedDate,
        DateTimeUtil.currentDateTimeFlow(context),
    ) { selectedDate, currentDateTime ->
        DayOfWeekSelectorUiState(
            currentDateTime = currentDateTime,
            selectedDate = selectedDate
        )
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = DayOfWeekSelectorUiState(
                currentDateTime = DateTimeUtil.currentDateTime,
                selectedDate = openScheduleDate ?: getActualDate()
            )
        )

    val hasNewFeatures = settings.data.map {
        it.newFeaturesVersion < BuildConfig.LAST_BIG_UPDATE_VERSION_CODE.toInt() &&
                uiState.value is HomeUiState.Success
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = false
        )

    fun onIntent(intent: HomeIntent) {
        when (intent) {
            is HomeIntent.ChangeDate -> {
                savedStateHandle[SELECTED_DATE] = intent.date.toEpochDays()
            }
            HomeIntent.NavigateToNewFeatures -> {
                viewModelScope.launch {
                    settings.updateDataVersions {
                        it.copy(newFeaturesVersion = BuildConfig.LAST_BIG_UPDATE_VERSION_CODE.toInt())
                    }
                    _events.send(HomeEvent.NavigateToGroupSettings)
                }
            }
            is HomeIntent.ChangeGroupView -> {
                _selectedGroup.value = intent.group
            }
            HomeIntent.NavigateToGroupSettings -> {
                _events.trySend(HomeEvent.NavigateToGroupSettings)
            }
        }
    }

    private fun getCurrentClassLabelState(
        currentDateTime: LocalDateTime,
        lessons: List<Lesson>
    ): TextResource? {
        if (lessons.isEmpty()) return null

        val firstClass = lessons.first()
        val firstClassStart = firstClass.startAt.atDate(firstClass.date)
        val timeDiffForFirstClass = DateTimeUtil.difference(
            currentDateTime,
            firstClassStart,
            ChronoUnit.MINUTES
        ).absoluteValue

        val currentClassIndex = lessons.indexOfFirst {
            val start = it.startAt.atDate(it.date)
            val end = it.endAt.atDate(it.date)
            currentDateTime in start..end
        }.takeIf { it != -1 }

        if (firstClassStart > currentDateTime) {
            return if (timeDiffForFirstClass <= 59) {
                TextResource.DynamicString(
                    R.string.the_first_class_will_start_in,
                    DateTimeUtil.formatRelativeDateTime(
                        timeDiffForFirstClass.toDouble(),
                        RelativeDateTimeFormatter.RelativeUnit.MINUTES
                    )
                )
            } else {
                val hoursBeforeFirstClass = (timeDiffForFirstClass / 60.0).roundToInt().toDouble()
                TextResource.DynamicString(
                    R.string.the_first_class_will_start_in,
                    DateTimeUtil.formatRelativeDateTime(
                        hoursBeforeFirstClass,
                        RelativeDateTimeFormatter.RelativeUnit.HOURS
                    )
                )
            }
        }

        if (lessons.getOrNull(currentClassIndex ?: -1) != null) {
            val startedClass = lessons[currentClassIndex!!]
            return TextResource.DynamicString(
                R.string.this_class_will_end_in,
                DateTimeUtil.formatRelativeAdaptiveTime(currentDateTime.time, startedClass.endAt)
            )
        }

        if (lessons.last().let { it.endAt.atDate(it.date) } < currentDateTime) {
            return TextResource.Id(R.string.classes_are_over)
        }

        val lastClassEnd = lessons.last().endDateTime
        val justEndedClass = lessons.lastOrNull { lesson ->
            currentDateTime >= lesson.endDateTime &&
                    lessons.firstOrNull { it.startDateTime > currentDateTime }?.startDateTime
                        ?.let { currentDateTime < it } ?: false
        } ?: return null

        val willStartClass = lessons.getOrNull(lessons.indexOf(justEndedClass) + 1)
            ?: return null
        val currDateTimeInBreak = lessons.fastAny { lesson ->
            currentDateTime in lesson.startDateTime..lesson.endDateTime
        }.not()
        if (currentDateTime in firstClassStart..lastClassEnd && currDateTimeInBreak) {

            return TextResource.DynamicString(
                R.string.study_break,
                DateTimeUtil.formatRelativeAdaptiveTime(
                    currentDateTime.time,
                    willStartClass.startAt
                )
            )
        }

        return null
    }

    private fun getActualDate(): LocalDate {
        val currentDate: LocalDate = DateTimeUtil.currentDate
        return if (currentDate.dayOfWeek != DayOfWeek.SUNDAY) {
            currentDate
        } else {
            currentDate.plus(1, DateTimeUnit.DAY)
        }
    }
}