
plugins {
    alias(libs.plugins.bgitu.library)
    alias(libs.plugins.bgitu.serialization)
    alias(libs.plugins.bgitu.koin)
    alias(libs.plugins.secrets)
}

android {
    namespace = "ru.bgitu.core.network"
    buildFeatures.buildConfig = true
    defaultConfig {
        consumerProguardFiles("consumer-proguard-rules.pro")
    }
}

dependencies {
    implementation(projects.core.model)
    implementation(projects.core.common)
    implementation(projects.core.datastore)

    implementation(libs.kotlinx.datetime)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.crashlytics)

    implementation(libs.ktor.core)
    implementation(libs.ktor.cio)
    implementation(libs.ktor.logging)
    implementation(libs.ktor.contentNegotiation)
    implementation(libs.ktor.kotlinx.json)
    implementation(libs.ktor.auth)
}