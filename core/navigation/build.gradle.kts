plugins {
    alias(libs.plugins.bgitu.library)
    alias(libs.plugins.bgitu.library.compose)
    alias(libs.plugins.bgitu.serialization)
}

android.namespace = "ru.bgitu.core.navigation"

dependencies {
    implementation(libs.compose.navigation)
    implementation(projects.core.common)
}