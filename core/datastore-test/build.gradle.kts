plugins {
    alias(libs.plugins.bgitu.library)
}

android.namespace = "ru.bgitu.core.datastore_test"

dependencies {
    implementation(projects.core.datastore)
    implementation(libs.datastore)
    implementation(libs.junit)
}