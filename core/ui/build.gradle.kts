
plugins {
    alias(libs.plugins.bgitu.library)
    alias(libs.plugins.bgitu.library.compose)
}

android.namespace = "ru.bgitu.core.ui"

dependencies {
    implementation(projects.core.designsystem)
    implementation(projects.core.common)
    implementation(projects.core.model)

    implementation(libs.kotlinx.datetime)
    implementation(libs.lifecycle.compose)
    implementation(libs.lifecycle.runtime.ktx)
}