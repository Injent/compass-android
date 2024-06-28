plugins {
    alias(libs.plugins.bgitu.feature)
    alias(libs.plugins.secrets)
}

android {
    namespace = "ru.bgitu.feature.login"

    buildFeatures.buildConfig = true

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        testOptions.animationsDisabled = true
    }
}

dependencies {
    testImplementation(projects.core.dataTest)

    androidTestImplementation(libs.core.test)
}