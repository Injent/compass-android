package ru.bgitu.feature.profile.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import org.koin.androidx.compose.koinViewModel
import ru.bgitu.core.navigation.LocalNavController
import ru.bgitu.core.navigation.Screen
import ru.bgitu.core.navigation.parent
import ru.bgitu.feature.profile.presentation.ProfileScreen

fun NavGraphBuilder.profile() {
    composable<Screen.Profile> {
        ProfileScreen(
            viewModel = koinViewModel(
                viewModelStoreOwner = it.parent(LocalNavController.current)
            )
        )
    }
}