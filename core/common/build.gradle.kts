
plugins {
    alias(libs.plugins.bgitu.library)
    alias(libs.plugins.bgitu.koin)
    alias(libs.plugins.bgitu.serialization)
    alias(libs.plugins.secrets)
}

android {
    namespace = "ru.bgitu.core.common"
    buildFeatures.buildConfig = true
}

dependencies {
    implementation(libs.androidx.annotation)
    implementation(libs.kotlinx.coroutines.android)
    api(libs.kotlinx.datetime)
}