package ru.bgitu.core.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

enum class Tab(
    @StringRes val label: Int,
    @DrawableRes val icon: Int,
) {
    HOME(
        label = R.string.label_schedule,
        icon = R.drawable.ic_calendar,
    ),
    PROFESSOR_SEARCH(
        label = R.string.label_professor_search,
        icon = R.drawable.ic_case,
    ),
//    MATES(
//        label = R.string.label_mates,
//        icon = R.drawable.ic_assigment,
//    ),
    PROFILE(
        label = R.string.label_profile,
        icon = 0, // unused
    )
}