package ru.bgitu.core.common

import android.annotation.SuppressLint
import android.content.Context

object LessonDataUtils {
    private val endings2 = setOf("ие", "ия", "ые", "ой", "ый")
    private val endings3 = setOf("ние", "сти", "ный")
    private const val CONSONANTS = "бвгджзйклмнпрстфхцчшщ"

    fun provideLocation(
        context: Context,
        building: String,
        classroom: String,
        style: DisplayStyle
    ): String {
        if (building.contains("ДОТ") || classroom.contains("ДОТ")) {
            return "ДОТ"
        }

        return if (classroom.isEmpty() && building.isNotEmpty()) {
            context.getString("location_building", style, building)
        } else {
            val buildingText = context.getString("location_building", style, building)
            val classroomText = context.getString("location_classroom", style, classroom)
            "$classroomText, $buildingText"
        }
    }

    fun shortenSentence(sentence: String): String {
        return sentence.split(" ").joinToString(" ") { word ->
            if (word.length > 6) {
                var trimmedWord = word.substring(0, 5)
                for (i in 5 until word.length) {
                    trimmedWord += word[i]
                    if (word[i].lowercase() in CONSONANTS) {
                        break
                    }
                }
                if (word.endsWithAny(endings3)) {
                    val ending = word.substringFromEnd(3)
                    if (trimmedWord.takeLast(3) != ending) {
                        "$trimmedWord-$ending"
                    } else "$trimmedWord."
                } else if (word.endsWithAny(endings2)) {
                    val ending = word.substringFromEnd(2)
                    if (trimmedWord.takeLast(2) != ending) {
                        return@joinToString "$trimmedWord-$ending"
                    }
                    if (word.length == 7) {
                        word
                    } else "$trimmedWord."
                } else {
                    "$trimmedWord."
                }
            } else {
                word
            }
        }
    }

    private fun String.substringFromEnd(positions: Int): String {
        return substring(this.length - positions)
    }

    private fun String.endsWithAny(suffix: Collection<String>): Boolean {
        return suffix.any { this.endsWith(it) }
    }

    @SuppressLint("DiscouragedApi")
    private fun Context.getString(id: String, style: DisplayStyle, arg: String): String {
        val resId = this.resources.getIdentifier(
            "${id}_${style.name.lowercase()}",
            "string",
            packageName
        )

        return this.getString(resId, arg)
    }

    enum class DisplayStyle {
        DEFAULT,
        SHORT
    }
}