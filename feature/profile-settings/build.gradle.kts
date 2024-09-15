plugins {
    alias(libs.plugins.bgitu.feature)
}

android.namespace = "ru.bgitu.feature.profile_settings"

dependencies {
    implementation(projects.core.datastore)
    implementation(projects.core.network)

    implementation(projects.feature.input)
}