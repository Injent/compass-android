plugins {
    alias(libs.plugins.bgitu.library)
    alias(libs.plugins.bgitu.serialization)
}

android.namespace = "ru.bgitu.core.model"

dependencies {
    implementation(libs.kotlinx.datetime)
}