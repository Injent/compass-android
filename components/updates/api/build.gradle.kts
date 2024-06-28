
plugins {
    alias(libs.plugins.bgitu.library)
    alias(libs.plugins.bgitu.serialization)
}

android.namespace = "ru.bgitu.components.updates.api"

dependencies {
    implementation(projects.core.common)
    implementation(libs.kotlinx.datetime)
    implementation(libs.kotlinx.coroutines.android)
}