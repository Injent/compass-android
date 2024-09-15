
plugins {
    alias(libs.plugins.bgitu.library)
    alias(libs.plugins.bgitu.koin)
    alias(libs.plugins.bgitu.serialization)
}

android.namespace = "ru.bgitu.components.updates.impl"

dependencies {
    implementation(projects.components.updates.api)
    implementation(projects.core.common)
    implementation(projects.core.data)
    implementation(projects.core.network)
    implementation(projects.core.model)
    implementation(projects.core.datastore)
    implementation(projects.core.notifications)
    
    implementation(libs.kotlinx.datetime)
    implementation(platform(libs.firebase.bom))
    implementation(libs.work)
    implementation(libs.lifecycle.livedata)
    implementation(libs.koin.workManager)
    implementation(libs.kotlinx.coroutines.android)

    rustoreImplementation(platform(libs.rustore.bom))
    rustoreImplementation(libs.rustore.update)
}