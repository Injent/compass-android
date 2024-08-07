plugins {
    alias(libs.plugins.bgitu.feature)
}

android.namespace = "ru.bgitu.feature.settings"

dependencies {
    implementation(projects.core.datastore)
    implementation(projects.feature.scheduleNotifier.api)
    implementation(libs.koin.navigation)
}