package ru.bgitu.feature.profile_settings.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import ru.bgitu.core.navigation.Screen
import ru.bgitu.feature.profile_settings.presentation.expert.ExpertApplyRoute
import ru.bgitu.feature.profile_settings.presentation.settings.ProfileSettingsRoute
import ru.bgitu.feature.profile_settings.presentation.variants.VariantsRoute

fun NavGraphBuilder.profileSettingsRoutes() {
    composable<Screen.ProfileSettings> {
        ProfileSettingsRoute()
    }
    expertApplyRoute()
}

private fun NavGraphBuilder.expertApplyRoute() {
    composable<Screen.ExpertApply> {
        ExpertApplyRoute()
    }
}

private fun NavGraphBuilder.variantRoute() {
    composable<Screen.ExpertApply> {
        val route = it.toRoute<Screen.VariantScreen>()

        VariantsRoute(
            subjectName = route.subjectName
        )
    }
}