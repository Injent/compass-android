plugins {
    alias(libs.plugins.bgitu.feature)
    alias(libs.plugins.secrets)
}

android {
    namespace = "ru.bgitu.feature.home"
    buildFeatures.buildConfig = true
}

dependencies {
    implementation(projects.core.datastore)

    implementation(libs.kotlinx.datetime)
    implementation(libs.sheetsComposeDialog.core)
    implementation(libs.sheetsComposeDialog.calendar)
}