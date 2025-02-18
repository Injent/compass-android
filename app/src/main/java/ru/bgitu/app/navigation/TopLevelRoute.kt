package ru.bgitu.app.navigation

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector
import ru.bgitu.app.R
import ru.bgitu.core.designsystem.icon.AppIcons
import ru.bgitu.core.designsystem.icon.Calendar
import ru.bgitu.core.designsystem.icon.Case
import ru.bgitu.core.navigation.Screen

class TopLevelRoute<T : Any>(
    @StringRes val name: Int,
    val route: T,
    val icon: ImageVector
)

val topLevelRoutes = listOf(
    TopLevelRoute(
        name = R.string.label_schedule,
        route = Screen.Home,
        icon = AppIcons.Calendar
    ),
    TopLevelRoute(
        name = R.string.label_teacher_search,
        route = Screen.TeacherSearch(),
        icon = AppIcons.Case
    ),
//    TopLevelRoute(
//        name = R.string.label_notes,
//        route = Screen.Notes(null),
//        icon = AppIcons.Calendar // unused placeholder
//    ),
    TopLevelRoute(
        name = R.string.label_profile,
        route = Screen.Profile,
        icon = AppIcons.Calendar // unused placeholder
    )
)