package ru.bgitu.feature.profile_settings.presentation.variants

import androidx.compose.runtime.Composable
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import ru.bgitu.core.navigation.LocalNavController

@Composable
fun VariantsRoute(subjectName: String) {
    val navController = LocalNavController.current

    val viewModel = koinViewModel<VariantViewModel> {
        parametersOf(subjectName)
    }
}

@Composable
private fun VariantScreen() {

}