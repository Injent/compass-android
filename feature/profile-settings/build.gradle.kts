plugins {
    alias(libs.plugins.bgitu.feature)
}

android.namespace = "ru.bgitu.feature.profile_settings"

dependencies {
    implementation(projects.core.datastore)

    implementation(projects.feature.input)
}