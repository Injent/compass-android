import java.util.Properties

plugins {
    alias(libs.plugins.bgitu.library)
    alias(libs.plugins.bgitu.koin)
    alias(libs.plugins.bgitu.serialization)
    alias(libs.plugins.secrets)
}

android {
    namespace = "ru.bgitu.components.signin"
    buildFeatures.buildConfig = true

    defaultConfig {
        val props = Properties().apply {
            rootProject.file("secrets.properties").inputStream().use { load(it) }
        }

        addManifestPlaceholders(
            mapOf(
                "VKIDClientID" to props.getProperty("VKIDClientID"),
                "VKIDClientSecret" to props.getProperty("VKIDClientSecret"),
                "VKIDRedirectHost" to "vk.com",
                "VKIDRedirectScheme" to "vk${props.getProperty("VKIDClientID")}",
            )
        )
    }
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