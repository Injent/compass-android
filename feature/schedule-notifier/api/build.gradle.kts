plugins {
    alias(libs.plugins.bgitu.library)
}

android.namespace = "ru.bgitu.feature.schedule_notifier.api"

dependencies {
    implementation(projects.core.model)
    implementation(libs.kotlinx.datetime)
}