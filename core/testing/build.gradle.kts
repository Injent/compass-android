plugins {
    alias(libs.plugins.bgitu.library)
    alias(libs.plugins.bgitu.library.compose)
}

android.namespace = "ru.bgitu.core.testing"

dependencies {

    api(kotlin("test"))
    api(projects.core.data)
    api(projects.core.datastore)
    api(projects.core.database)
    api(projects.core.network)
    api(projects.core.model)
    api(projects.core.common)
    api(libs.compose.ui.test)

    debugApi(libs.compose.ui.testManifest)

    implementation(libs.androidx.test.rules)
    implementation(projects.core.designsystem)
}