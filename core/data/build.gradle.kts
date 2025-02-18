plugins {
    alias(libs.plugins.bgitu.library)
    alias(libs.plugins.bgitu.koin)
}

android {
    namespace = "ru.bgitu.core.data"

    defaultConfig {
        consumerProguardFiles("consumer-proguard-rules.pro")
    }
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.datastore)
    implementation(projects.core.network)
    implementation(projects.core.model)
    implementation(projects.core.notifications)

    implementation(libs.kotlinx.serialization)
    implementation(libs.kotlinx.datetime)
    implementation(libs.room)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.messaging)

    huaweiImplementation(libs.huawei.pushKit)

    rustoreImplementation(platform(libs.rustore.bom))
    rustoreImplementation(libs.rustore.push)

    implementation(libs.ktor.core)
    
    testImplementation(libs.datastore)

    androidTestImplementation(libs.datastore)
    androidTestImplementation(libs.robolectric)
}