package ru.bgitu.feature.findmate.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import org.koin.androidx.compose.koinViewModel
import ru.bgitu.core.navigation.LocalNavController
import ru.bgitu.core.navigation.Screen
import ru.bgitu.core.navigation.parent
import ru.bgitu.feature.findmate.presentation.search.SearchMateRoute

fun NavGraphBuilder.findMateGraph() {
    composable<Screen.SearchMate> {
        SearchMateRoute(
            viewModel = koinViewModel(
                viewModelStoreOwner = it.parent(LocalNavController.current)
            )
        )
    }
}