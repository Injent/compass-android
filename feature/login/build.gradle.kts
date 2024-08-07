plugins {
    alias(libs.plugins.bgitu.feature)
    alias(libs.plugins.secrets)
    alias(libs.plugins.bgitu.vkplaceholders)
}

android {
    namespace = "ru.bgitu.feature.login"

    buildFeatures.buildConfig = true
}

dependencies {
    implementation(projects.components.signin)

    implementation(libs.vk.id.oneTapCompose)
    implementation(libs.vk.id)
}