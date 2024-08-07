plugins {
    alias(libs.plugins.bgitu.library)
    alias(libs.plugins.bgitu.koin)
    alias(libs.plugins.bgitu.serialization)
    alias(libs.plugins.secrets)
    alias(libs.plugins.bgitu.vkplaceholders)
}

android {
    namespace = "ru.bgitu.components.signin"
    buildFeatures.buildConfig = true
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.model)
    implementation(projects.core.data)
    implementation(projects.core.datastore)
    implementation(projects.core.database)
    implementation(projects.core.network)

    implementation(libs.room)
    implementation(libs.ktor.core)
    implementation(libs.ktor.cio)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth)
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.playServicesAuth)
    implementation(libs.google.identify)

    implementation(libs.vk.id)
}