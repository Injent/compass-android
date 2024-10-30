plugins {
    alias(libs.plugins.bgitu.feature)
    alias(libs.plugins.bgitu.serialization)
}

android.namespace = "ru.bgitu.feature.notes"

dependencies {
    implementation(projects.core.database)
}