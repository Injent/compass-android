plugins {
    alias(libs.plugins.bgitu.feature)
}

android.namespace = "ru.bgitu.feature.groups"

dependencies {
    implementation(projects.core.datastore)
    implementation(projects.core.network)

    implementation(libs.reorderable)
}