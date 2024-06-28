package ru.bgitu.feature.groups.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import ru.bgitu.core.navigation.Screen
import ru.bgitu.feature.groups.model.getPrimaryGroup
import ru.bgitu.feature.groups.model.getSecondaryGroup
import ru.bgitu.feature.groups.presentation.groups.GroupsRoute
import ru.bgitu.feature.groups.presentation.search.GroupSearchRoute

fun NavGraphBuilder.groupsScreen() {
    composable<Screen.Groups> {
        GroupsRoute(
            backResultPrimaryGroup = it.savedStateHandle.getPrimaryGroup(),
            backResultSecondaryGroup = it.savedStateHandle.getSecondaryGroup()
        )
    }
    composable<Screen.GroupSearch> {
        val route = it.toRoute<Screen.GroupSearch>()
        GroupSearchRoute(
            backResultType = route.backResultType
        )
    }
}