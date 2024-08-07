plugins {
    alias(libs.plugins.bgitu.library)
    alias(libs.plugins.bgitu.serialization)
}

android.namespace = "ru.bgitu.core.model"
android.defaultConfig {
    consumerProguardFiles("consumer-proguard-rules.pro")
}

dependencies {
    implementation(libs.kotlinx.datetime)
}