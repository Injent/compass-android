package ru.bgitu.core.common

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.icu.text.RelativeDateTimeFormatter
import android.icu.text.RelativeDateTimeFormatter.Direction
import android.icu.text.RelativeDateTimeFormatter.RelativeUnit
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atTime
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toJavaLocalTime
import kotlinx.datetime.toLocalDateTime
import java.time.Month
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.time.format.TextStyle
import java.time.temporal.ChronoUnit
import java.util.Locale
import kotlin.math.absoluteValue

object DateTimeUtil {
    private val hoursMinutesFormatter by lazy { DateTimeFormatter.ofPattern("HH:mm") }
    private val relativeDateTimeFormatter by lazy { RelativeDateTimeFormatter.getInstance() }
    private val dateFormatter by lazy { DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL) }
    private val monthFormatter by lazy { DateTimeFormatter.ofPattern("d MMM") }

    val currentDate: LocalDate
        get() = currentDateTime.date

    val currentDateTime: LocalDateTime
        get() = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

    val weeksDateBoundary: ClosedRange<LocalDate>
        get() {
            val from = getStartOfWeek(currentDate.minus(2, DateTimeUnit.WEEK))
            val to = getEndOfWeek(currentDate.plus(2, DateTimeUnit.WEEK))
            return from..to
        }

    fun currentDateTimeFlow(context: Context): Flow<LocalDateTime> = callbackFlow {
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                if (intent.action != Intent.ACTION_TIME_TICK) return

                trySend(currentDateTime)
            }
        }
        context.registerReceiver(receiver, IntentFilter(Intent.ACTION_TIME_TICK))

        awaitClose {
            context.unregisterReceiver(receiver)
        }
    }
        .onStart { emit(currentDateTime) }

    private fun getStartOfWeek(date: LocalDate): LocalDate {
        var localDate = date

        while (localDate.dayOfWeek != DayOfWeek.MONDAY) {
            localDate = localDate.minus(1, DateTimeUnit.DAY)
        }

        return localDate
    }

    fun getEndOfWeek(date: LocalDate): LocalDate {
        var localDate = date

        while (localDate.dayOfWeek != DayOfWeek.SATURDAY) {
            localDate = localDate.plus(1, DateTimeUnit.DAY)
        }

        return localDate
    }

    fun getWeekDates(fromDate: LocalDate, includeWeekendDays: Boolean = false): List<LocalDate> {
        val startOfWeek = getStartOfWeek(fromDate)
        val days = (0..6).map { weekDayOffset ->
            startOfWeek.plus(weekDayOffset, DateTimeUnit.DAY)
        }.sortedBy { it.dayOfWeek.value }

        return if (includeWeekendDays)
            days
        else days.filter { it.dayOfWeek != DayOfWeek.SUNDAY }
    }

    fun formatDay(date: LocalDate): String {
        val startOfWeek = getStartOfWeek(date)
        val endOfWeek = startOfWeek.plus(DayOfWeek.entries.size, DateTimeUnit.DAY)

        val useWeekNaming = currentDate in startOfWeek..endOfWeek
        return if (useWeekNaming) {
            date.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())
        } else {
            date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()) + ", " + monthFormatter.format(date.toJavaLocalDate())
        }
    }

    fun formatTime(localTime: LocalTime): String {
        return hoursMinutesFormatter.format(localTime.toJavaLocalTime())
    }

    fun formatDate(localDate: LocalDate): String {
        return dateFormatter.format(localDate.toJavaLocalDate())
    }

    fun formatWeek(date: LocalDate, short: Boolean = true): String {
        return date.dayOfWeek.getDisplayName(if (short) TextStyle.SHORT else TextStyle.FULL, Locale.getDefault())
    }

    fun formatRelativeDateTime(quantity: Double, relativeUnit: RelativeUnit, direction: Direction = Direction.NEXT): String {
        return relativeDateTimeFormatter.format(
            quantity,
            direction,
            relativeUnit
        )
    }

    private fun formatThis(
        quantity: Double,
        relativeUnit: RelativeUnit,
        direction: Direction = Direction.NEXT
    ): String {
        val s = formatRelativeDateTime(quantity, relativeUnit, direction)
        return if (direction == Direction.NEXT) {
            val index = s.indexOfFirst { it == ' ' } + 1
            s.substring(index)
        } else {
            s.substringBeforeLast(' ')
        }
    }

    fun formatRelativeAdaptiveDateTime(
        context: Context,
        from: LocalDateTime,
        to: LocalDateTime,
        direction: Direction = Direction.NEXT
    ): String {
        val dayDiff = (from.date.toEpochDays() - to.date.toEpochDays()).absoluteValue
        return if (dayDiff > 0) {
            relativeDateTimeFormatter.format(
                dayDiff.toDouble(),
                direction,
                RelativeUnit.DAYS
            )
        } else {
            formatRelativeAdaptiveTime(context, from.time, to.time, direction)
        }
    }

    @Deprecated("use Context.formatRelativeAdaptiveDateTime instead")
    fun formatRelativeAdaptiveDateTime(
        from: LocalDateTime,
        to: LocalDateTime,
        direction: Direction = Direction.NEXT,
    ): String {
        val dayDiff = difference(from, to, ChronoUnit.DAYS).absoluteValue

        if (dayDiff > 0) {
            return relativeDateTimeFormatter.format(
                dayDiff.toDouble(),
                direction,
                RelativeUnit.DAYS
            )
        }

        return formatRelativeAdaptiveTime(from.time, to.time, direction)
    }

    private fun formatRelativeAdaptiveTime(
        context: Context,
        from: LocalTime,
        to: LocalTime,
        direction: Direction = Direction.NEXT
    ): String {
        val hoursDiff = difference(from, to, ChronoUnit.HOURS).absoluteValue
        val minutesDiff = difference(from, to, ChronoUnit.MINUTES).absoluteValue.let {
            if (hoursDiff > 0) {
                it / 60
            } else it
        }
        val secondsDiff = difference(from, to, ChronoUnit.SECONDS).absoluteValue

        val sb = StringBuilder(32)

        if (hoursDiff > 0) {
            when {
                direction == Direction.LAST && minutesDiff >= 10 -> {
                    sb.append(context.resources.getQuantityString(R.plurals.hours, hoursDiff, hoursDiff))
                }
                else -> {
                    sb.append(
                        relativeDateTimeFormatter.format(
                            hoursDiff.toDouble(),
                            direction,
                            RelativeUnit.HOURS
                        )
                    )
                }
            }
            sb.append(" ")
        }

        when {
            direction == Direction.NEXT && minutesDiff >= 10 && hoursDiff > 0 -> {
                sb.append(context.resources.getQuantityString(R.plurals.minutes, minutesDiff, minutesDiff))
            }
            minutesDiff < 10 && hoursDiff > 0 -> Unit
            else -> {
                sb.append(
                    relativeDateTimeFormatter.format(
                        minutesDiff.toDouble(),
                        direction,
                        RelativeUnit.MINUTES
                    )
                )
            }
        }
        if (minutesDiff == 0) {
            relativeDateTimeFormatter.format(
                secondsDiff.toDouble(),
                direction,
                RelativeUnit.SECONDS
            )
        }
        return sb.toString()
    }

    fun formatRelativeAdaptiveTime(
        from: LocalTime,
        to: LocalTime,
        direction: Direction = Direction.NEXT
    ): String {
        val hourDiff = difference(from, to, ChronoUnit.HOURS).absoluteValue
        val minuteDiff = difference(from, to, ChronoUnit.MINUTES).absoluteValue
        val secondDiff = difference(from, to, ChronoUnit.SECONDS)

        val sb = StringBuilder()
        if (hourDiff > 0) {
            sb.append(
                relativeDateTimeFormatter.format(
                    hourDiff.toDouble(),
                    direction,
                    RelativeUnit.HOURS,
                )
            )
            sb.append(" ")
        }
        if (minuteDiff >= 0) {
            if (hourDiff > 0) {
                sb.append(
                    formatThis(
                        (minuteDiff + 1) - (hourDiff * 60.0),
                        RelativeUnit.MINUTES,
                        direction
                    )
                )
            } else {
                if (minuteDiff == 0) {
                    sb.append(
                        relativeDateTimeFormatter.format(
                            secondDiff.toDouble(),
                            direction,
                            RelativeUnit.SECONDS,
                        )
                    )
                } else {
                    sb.append(
                        relativeDateTimeFormatter.format(
                            minuteDiff.toDouble() + 1,
                            direction,
                            RelativeUnit.MINUTES
                        )
                    )
                }
            }
        }
        return sb.trimEnd().toString()
    }

    fun formatMinutes(context: Context, minutes: Int): String {
        return context.resources.getQuantityString(R.plurals.minutes, minutes, minutes)
    }

    fun formatMonth(localDate: LocalDate): String {
        return localDate.month.getDisplayName(TextStyle.FULL_STANDALONE, Locale.getDefault())
    }

    private fun difference(start: LocalTime, end: LocalTime, chronoUnit: ChronoUnit): Int {
        return chronoUnit.between(start.toJavaLocalTime(), end.toJavaLocalTime()).toInt()
    }

    fun difference(start: LocalDateTime, end: LocalDateTime, chronoUnit: ChronoUnit): Int {
        return chronoUnit.between(start.toJavaLocalDateTime(), end.toJavaLocalDateTime()).toInt()
    }

//        val startStudyYear = date.year.let {
//            if (date.monthNumber in 1..8)
//                it - 1
//            else it
//        }
//
//        var startDate = LocalDate(startStudyYear, Month.SEPTEMBER, 1)
//        while (startDate.dayOfWeek in arrayOf(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY)) {
//            startDate = startDate.plus(1, DateTimeUnit.DAY)
//        }
//        startDate = startDate.plus(1, DateTimeUnit.DAY)
//
//        val endOfCurrentWeek = date.toJavaLocalDate().with(DayOfWeek.SUNDAY).toKotlinLocalDate()
//
//        var weeksCount = 0
//        while (startDate < endOfCurrentWeek) {
//            if (startDate.dayOfWeek == DayOfWeek.SUNDAY) {
//                startDate = startDate.plus(1, DateTimeUnit.WEEK)
//            }
//            startDate = startDate.toJavaLocalDate().with(DayOfWeek.SUNDAY).toKotlinLocalDate()
//            weeksCount++
//        }
//
//        return weeksCount

    fun LocalDate.getStudyWeekNum(): Int {
        val thisWeekStartDate = this.minus(this.dayOfWeek.ordinal, DateTimeUnit.DAY)

        val startStudyYear = if (this.month.ordinal < Month.SEPTEMBER.ordinal) this.year - 1 else this.year

        val startStudyDate = LocalDate(startStudyYear, Month.SEPTEMBER, 1).let {
            val is1SepAtWeekend = it.dayOfWeek == DayOfWeek.SATURDAY || it.dayOfWeek == DayOfWeek.SUNDAY
            val startWeek = if (is1SepAtWeekend) it.plus(1, DateTimeUnit.WEEK) else it
            startWeek.minus(startWeek.dayOfWeek.ordinal, DateTimeUnit.DAY)
        }

        return (thisWeekStartDate.toEpochDays() - startStudyDate.toEpochDays()) / 7 + 1
    }

    fun LocalDate.isOddWeek(): Boolean {
        return this.getStudyWeekNum() % 2 != 0
    }

    fun getFormattedStudyWeekNumber(date: LocalDate): TextResource {
        return TextResource.DynamicString(
            R.string.week,
            date.getStudyWeekNum().toString()
        )
    }
}

fun LocalDate.atStartOfStudyDay(): LocalDateTime {
    return plus(1, DateTimeUnit.DAY).atTime(7, 20)
}

fun ClosedRange<LocalDate>.iterator(step: Int = 1, unit: DateTimeUnit.DateBased): Iterator<LocalDate> {
    var dateIn = start.minus(step, unit)
    return object : Iterator<LocalDate> {
        override fun hasNext() = dateIn < this@iterator.endInclusive
        override fun next() = dateIn.plus(step, unit).also { dateIn = it }
    }
}