
plugins {
    alias(libs.plugins.bgitu.feature)
}

android.namespace = "ru.bgitu.feature.profile"

dependencies {
    implementation(projects.core.datastore)
    implementation(projects.feature.scheduleNotifier.api)
    implementation(projects.feature.scheduleWidget)
}