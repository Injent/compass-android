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
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.atDate
import kotlinx.datetime.atTime
import kotlinx.datetime.plus
import ru.bgitu.core.common.DateTimeUtil
import ru.bgitu.core.common.TextResource
import ru.bgitu.core.common.eventChannel
import ru.bgitu.core.common.eventbus.EventBus
import ru.bgitu.core.common.eventbus.GlobalAppEvent
import ru.bgitu.core.data.model.ScheduleState
import ru.bgitu.core.data.model.getLessons
import ru.bgitu.core.data.repository.ScheduleRepository
import ru.bgitu.core.data.util.NetworkMonitor
import ru.bgitu.core.data.util.SyncManager
import ru.bgitu.core.data.util.SyncStatus
import ru.bgitu.core.datastore.SettingsRepository
import ru.bgitu.core.datastore.model.StoredLesson
import ru.bgitu.core.model.Group
import ru.bgitu.feature.home.R
import ru.bgitu.feature.home.impl.model.DayOfWeekSelectorUiState
import java.time.temporal.ChronoUnit
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

private const val SELECTED_DATE = "selected_date"

data class ChangeGroupView(val group: Group)

sealed interface HomeIntent {
    data class ChangeDate(val date: LocalDate) : HomeIntent
    data class ChangeGroupView(val group: Group) : HomeIntent
    data object NavigateToGroupSettings : HomeIntent
    data class NavigateToCreateNote(val subjectName: String) : HomeIntent
    data object DismissPickGroupQuery : HomeIntent
    data object PickGroup : HomeIntent
}

sealed interface HomeEvent {
    data object NavigateToGroupSettings : HomeEvent
    data object NavigateToNewFeatures : HomeEvent
    data class NavigateToCreateNote(val subjectName: String) : HomeEvent
}

sealed interface HomeUiState {
    data class Success(
        val selectedGroup: Group? = null,
        val savedGroups: List<Group> = emptyList(),
        val showGroups: Boolean,
        val syncStatus: SyncStatus = SyncStatus.IDLE,
    ) : HomeUiState
    data class GroupNotSelected(val shouldShowDataResetAlert: Boolean) : HomeUiState
    data object Loading : HomeUiState
}

class HomeViewModel(
    private val savedStateHandle: SavedStateHandle,
    openScheduleDate: LocalDate?,
    private val settings: SettingsRepository,
    scheduleRepository: ScheduleRepository,
    syncManager: SyncManager,
    networkMonitor: NetworkMonitor,
    context: Context,
) : ViewModel() {
    private val _events = eventChannel<HomeEvent>()
    val events = _events.receiveAsFlow()

    private val _selectedGroup = MutableStateFlow(
        runBlocking { settings.data.first().primaryGroup }
    )

    val uiState = combine(
        _selectedGroup,
        settings.data,
        syncManager.syncState,
        networkMonitor.isOnline,
    ) { selectedGroup, userdata, syncStatus, isOnline ->
        if (userdata.primaryGroup == null) {
            return@combine HomeUiState.GroupNotSelected(userdata.shouldShowDataResetAlert)
        }
        HomeUiState.Success(
            selectedGroup = selectedGroup,
            savedGroups = listOf(userdata.primaryGroup!!) + userdata.userPrefs.savedGroups,
            showGroups = userdata.userPrefs.showGroupsOnMainScreen,
            syncStatus = if (!isOnline) SyncStatus.FAILED else syncStatus,
        )
    }
        .onStart {
            viewModelScope.launch {
                EventBus.subscribe<GlobalAppEvent.ChangePrimaryGroup> {
                    _selectedGroup.value = settings.data.first().primaryGroup
                }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = HomeUiState.Loading
        )

    private val _selectedDate = savedStateHandle.getStateFlow(
        SELECTED_DATE, (openScheduleDate ?: DateTimeUtil.currentDate).toEpochDays()
    )
        .map {
            LocalDate.fromEpochDays(it).skipSunday()
        }

    val scheduleUiState = combine(
        scheduleRepository.schedule,
        _selectedGroup,
        settings.data.map { it.primaryGroup },
        _selectedGroup.flatMapLatest { group ->
            if (group != settings.data.first().primaryGroup) {
                group?.let {
                    scheduleRepository.getNetworkLessonsFlow(it.id)
                } ?: flowOf(ScheduleState.Loading)
            } else flowOf(ScheduleState.Loading)
        }
    ) { schedule, selectedGroup, primaryGroup, networkSchedule ->
        if (selectedGroup == null) return@combine ScheduleState.Loading

        if (selectedGroup == primaryGroup) {
            schedule
        } else networkSchedule

    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ScheduleState.Loading
        )

    val classLabelMessage: StateFlow<TextResource?> = combine(
        scheduleUiState,
        DateTimeUtil.currentDateTimeFlow(context),
    ) { scheduleState, now ->
        if (scheduleState is ScheduleState.Loaded) {
            getCurrentClassLabelState(now, scheduleState.getLessons(now.date))
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
    ) { selectedDate, now ->
        DayOfWeekSelectorUiState(
            now = now.skipSunday(),
            selectedDate = selectedDate.skipSunday(),
            initialDate = openScheduleDate?.skipSunday()
        )
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = DayOfWeekSelectorUiState(
                now = DateTimeUtil.currentDateTime.skipSunday(),
                selectedDate = (openScheduleDate ?: DateTimeUtil.currentDate).skipSunday(),
                initialDate = openScheduleDate?.skipSunday()
            )
        )

    fun onIntent(intent: HomeIntent) {
        when (intent) {
            is HomeIntent.ChangeDate -> {
                println("DATE CHANGED ${intent.date}")
                savedStateHandle[SELECTED_DATE] = intent.date.toEpochDays()
            }
            is HomeIntent.ChangeGroupView -> {
                _selectedGroup.value = intent.group
            }
            HomeIntent.NavigateToGroupSettings -> {
                _events.trySend(HomeEvent.NavigateToGroupSettings)
            }
            is HomeIntent.DismissPickGroupQuery -> viewModelScope.launch {
                settings.updateMetadata {
                    it.copy(shouldShowDataResetAlert = false)
                }
            }
            is HomeIntent.PickGroup -> viewModelScope.launch {
                settings.updateMetadata {
                    it.copy(shouldShowDataResetAlert = false)
                }
                _events.send(HomeEvent.NavigateToGroupSettings)
            }
            is HomeIntent.NavigateToCreateNote -> Unit
        }
    }

    private fun getCurrentClassLabelState(
        now: LocalDateTime,
        lessons: List<StoredLesson>
    ): TextResource? {
        if (lessons.isEmpty()) return null

        val firstClass = lessons.first()
        val firstClassStart = firstClass.startAt.atDate(now.date)
        val timeDiffForFirstClass = DateTimeUtil.difference(
            now,
            firstClassStart,
            ChronoUnit.MINUTES
        ).absoluteValue

        val currentClassIndex = lessons.indexOfFirst {
            val start = it.startAt.atDate(now.date)
            val end = it.endAt.atDate(now.date)
            now in start..end
        }.takeIf { it != -1 }

        if (firstClassStart > now) {
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
                DateTimeUtil.formatRelativeAdaptiveTime(now.time, startedClass.endAt)
            )
        }

        if (lessons.last().endAt.atDate(now.date) < now) {
            return TextResource.Id(R.string.classes_are_over)
        }

        val lastClassEnd = lessons.last().endAt.atDate(now.date)
        val justEndedClass = lessons.lastOrNull { lesson ->
            now >= lesson.endAt.atDate(now.date) &&
                    lessons.firstOrNull {
                        it.startAt.atDate(now.date) > now
                    }
                        ?.startAt?.atDate(now.date)
                        ?.let { now < it } ?: false
        } ?: return null

        val willStartClass = lessons.getOrNull(lessons.indexOf(justEndedClass) + 1)
            ?: return null
        val currDateTimeInBreak = lessons.fastAny { lesson ->
            now in lesson.startAt.atDate(now.date)..lesson.endAt.atDate(now.date)
        }.not()
        if (now in firstClassStart..lastClassEnd && currDateTimeInBreak) {
            return TextResource.DynamicString(
                R.string.study_break,
                DateTimeUtil.formatRelativeAdaptiveTime(
                    now.time,
                    willStartClass.startAt
                )
            )
        }

        return null
    }

    override fun onCleared() {
        super.onCleared()
        println("HOME VIEW MODEL CLEARED")
    }
}

fun LocalDate.skipSunday(): LocalDate {
    return if (dayOfWeek == DayOfWeek.SUNDAY) {
        this.plus(1, DateTimeUnit.DAY)
    } else this
}

fun LocalDateTime.skipSunday(): LocalDateTime {
    return if (dayOfWeek == DayOfWeek.SUNDAY) {
        this.date.plus(1, DateTimeUnit.DAY).atTime(time)
    } else this
}