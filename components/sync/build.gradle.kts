
plugins {
    alias(libs.plugins.bgitu.library)
    alias(libs.plugins.bgitu.koin)
}

android.namespace = "ru.bgitu.components.sync"

dependencies {
    implementation(projects.core.data)
    implementation(projects.core.model)
    implementation(projects.core.datastore)
    implementation(projects.core.common)
    implementation(projects.core.notifications)

    implementation(libs.work)
    implementation(libs.lifecycle.livedata)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.datetime)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.messaging)
}