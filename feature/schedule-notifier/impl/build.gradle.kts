plugins {
    alias(libs.plugins.bgitu.feature)
    alias(libs.plugins.bgitu.serialization)
}

android.namespace = "ru.bgitu.feature.schedule_notifier.impl"

dependencies {
    implementation(projects.feature.scheduleNotifier.api)
    implementation(projects.core.notifications)
    implementation(projects.core.data)
    implementation(projects.core.datastore)

    implementation(libs.kotlinx.datetime)
}