package ru.bgitu.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.compose.currentBackStackEntryAsState

class OneTimeSavedStateHandle internal constructor(
    private val savedStateHandle: SavedStateHandle
) {
    fun <T> onResult(key: String, block: (T) -> Unit) {
        if (!savedStateHandle.contains(key)) {
            return
        }
        savedStateHandle.remove<T>(key)?.let(block)
    }
}

@Composable
fun BackResultEffect(onResult: OneTimeSavedStateHandle.() -> Unit) {
    val backStackEntry by LocalNavController.current.currentBackStackEntryAsState()

    LaunchedEffect(backStackEntry?.savedStateHandle) {
        backStackEntry?.savedStateHandle?.let { savedState ->
            onResult(OneTimeSavedStateHandle(savedState))
        }
    }
}