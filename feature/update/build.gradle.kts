plugins {
    alias(libs.plugins.bgitu.feature)
}

android.namespace = "ru.bgitu.feature.update"

dependencies {
    implementation(projects.components.updates.api)
    implementation(projects.core.datastore)
    implementation(libs.markdown)
}