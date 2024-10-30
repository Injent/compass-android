package ru.bgitu.core.database.util

import androidx.room.TypeConverter
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

class LocalTimeConverter {
    @TypeConverter
    fun localTimeToSeconds(localTime: LocalTime): Int {
        return localTime.toSecondOfDay()
    }
    @TypeConverter
    fun secondsToLocalTime(seconds: Int): LocalTime {
        return LocalTime.fromSecondOfDay(seconds)
    }
}

class LocalDateConverter {
    @TypeConverter
    fun localDateToDays(localDate: LocalDate): Int {
        return localDate.toEpochDays()
    }
    @TypeConverter
    fun daysToLocalDate(days: Int): LocalDate {
        return LocalDate.fromEpochDays(days)
    }
}

class LocalDateTimeConverter {
    @TypeConverter
    fun localDateTimeToSeconds(localDateTime: LocalDateTime): Long {
        return localDateTime.toInstant(TimeZone.currentSystemDefault()).epochSeconds
    }
    @TypeConverter
    fun secondsToLocalTimeDate(seconds: Long): LocalDateTime {
        return Instant.fromEpochSeconds(seconds).toLocalDateTime(TimeZone.currentSystemDefault())
    }
}