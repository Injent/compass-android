
plugins {
    alias(libs.plugins.bgitu.library)
    alias(libs.plugins.bgitu.koin)
}

android.namespace = "ru.bgitu.core.domain"

dependencies {
    implementation(libs.kotlinx.datetime)
    implementation(libs.kotlinx.coroutines.android)
    implementation(projects.core.data)
    implementation(projects.core.datastore)
    implementation(projects.core.network)
    implementation(projects.core.model)
}