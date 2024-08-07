
plugins {
    alias(libs.plugins.bgitu.feature)
    alias(libs.plugins.bgitu.vkplaceholders)
}

android.namespace = "ru.bgitu.feature.profile"

dependencies {
    implementation(projects.core.datastore)
    implementation(projects.components.signin)
    implementation(projects.feature.scheduleNotifier.api)
}