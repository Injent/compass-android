package ru.bgitu.core.navigation

import android.annotation.SuppressLint
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.serialization.generateHashCode
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.serializer

val LocalNavController = staticCompositionLocalOf<NavController> {
    error("Navigator not provided")
}

fun NavController.navigateTopDestination(tab: Tab) {
    val navigatingId = when(tab) {
        Tab.HOME -> getId<Screen.Home>()
        Tab.PROFESSOR_SEARCH -> getId<Screen.ProfessorSearch>()
        Tab.MATES -> getId<Screen.SearchMate>()
        Tab.PROFILE -> getId<Screen.Profile>()
    }

    if (currentDestination?.id == navigatingId) return

    navigate(
        when (tab) {
            Tab.HOME -> Screen.Home
            Tab.PROFESSOR_SEARCH -> Screen.ProfessorSearch()
            Tab.MATES -> Screen.SearchMate
            Tab.PROFILE -> Screen.ProfileGraph
        }
    ) {
        popUpTo(graph.findStartDestination().id) {
            saveState = true
        }

        launchSingleTop = true
        restoreState = true
    }
}

fun NavController.replaceAll(destination: Screen) {
    navigate(destination) {
        popUpTo(graph.findStartDestination().id) {
            inclusive = true
        }
    }
}

@OptIn(InternalSerializationApi::class)
fun NavController.push(destination: Any) {
    if (currentDestination?.id == destination::class.serializer().hashCode()) return
    navigate(destination)
}

fun NavController.back(): Boolean {
    return if (currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED) {
        navigateUp()
    } else false
}

fun NavBackStackEntry.parent(navController: NavController): NavBackStackEntry {
    return runCatching {
        navController.getBackStackEntry(
            requireNotNull(this.destination.parent?.route) {
                "Route can't be null. Error when building navigation tree"
            }
        )
    }.getOrDefault(this)
}

@SuppressLint("RestrictedApi")
@OptIn(InternalSerializationApi::class)
inline fun <reified T : Screen> getId(): Int {
    return T::class.serializer().generateHashCode()
}