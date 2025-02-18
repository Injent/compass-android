package ru.bgitu.feature.update.di

import com.google.firebase.Firebase
import com.google.firebase.crashlytics.crashlytics
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.bgitu.feature.update.presentation.AppUpdateViewModel

val AppUpdateModule = module {
    viewModel {
        val s = it.values.joinToString {
            "${runCatching { (it!!::class.qualifiedName) }.getOrDefault("null")}: $it"
        }
        Firebase.crashlytics.recordException(
            RuntimeException(s)
        )
        AppUpdateViewModel(
            appUpdateManager = get(),
            compassRepository = get(),
            updateVersionCode = it.get()
        )
    }
}