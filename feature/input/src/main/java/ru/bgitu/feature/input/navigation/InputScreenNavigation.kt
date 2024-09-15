package ru.bgitu.feature.input.navigation

import androidx.compose.foundation.text.input.InputTransformation
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable
import ru.bgitu.core.navigation.push
import ru.bgitu.feature.input.InputScreenRoute

data class InputParams(
    val title: String,
    val description: String,
    val resultKey: String,
    val initialText: String,
    val placeholder: String = "",
    val maxLines: Int = 1,
    val inputTransformation: InputTransformation = InputTransformation {}
)

fun InputParams.toRoute(): Input {
    return Input(
        title = title,
        description = description,
        resultKey = resultKey,
        initialText = initialText,
        placeholder = placeholder,
        maxLines = maxLines
    )
}

@Serializable
data class Input(
    val title: String,
    val description: String,
    val resultKey: String,
    val initialText: String,
    val placeholder: String = "",
    val maxLines: Int = 1,
) {
    companion object {
        internal var validator = InputTransformation {}
    }
}

fun NavGraphBuilder.inputRoute() {
    composable<Input> { backStackEntry ->
        val params = backStackEntry.toRoute<Input>()
        InputScreenRoute(params = params)
    }
}

fun NavController.navigateToInput(
    params: InputParams
) {
    Input.validator = params.inputTransformation
    push(params.toRoute())
}