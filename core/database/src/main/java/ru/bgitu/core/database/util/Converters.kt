package ru.bgitu.core.database.util

import androidx.room.TypeConverter
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

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