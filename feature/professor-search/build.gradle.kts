plugins {
    alias(libs.plugins.bgitu.feature)
    alias(libs.plugins.bgitu.serialization)
}

android.namespace = "ru.bgitu.feature.professor_search"

dependencies {
    implementation(projects.core.datastore)
    
    implementation(libs.kotlinx.datetime)

    testImplementation(projects.core.testing)
    testImplementation(libs.junit)
}