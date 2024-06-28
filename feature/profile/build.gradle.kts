
plugins {
    alias(libs.plugins.bgitu.feature)
}

android.namespace = "ru.bgitu.feature.profile"

dependencies {
    implementation(projects.feature.scheduleNotifier.api)
}