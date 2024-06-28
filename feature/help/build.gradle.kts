plugins {
    alias(libs.plugins.bgitu.feature)
    alias(libs.plugins.secrets)
}

android {
    namespace = "ru.bgitu.feature.help"
    buildFeatures.buildConfig = true
}