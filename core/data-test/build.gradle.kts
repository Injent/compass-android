plugins {
    alias(libs.plugins.bgitu.library)
}

android.namespace = "ru.bgitu.core.data_test"

dependencies {
    implementation(projects.core.data)
    implementation(libs.kotlinx.coroutines.android)
}