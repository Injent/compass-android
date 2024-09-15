plugins {
    alias(libs.plugins.bgitu.feature)
}

android.namespace = "ru.bgitu.feature.onboarding"

dependencies {
    implementation(projects.core.datastore)
}