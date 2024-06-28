
plugins {
    alias(libs.plugins.bgitu.library)
    alias(libs.plugins.bgitu.koin)
    alias(libs.plugins.ksp)
}

android.namespace = "ru.bgitu.core.database"

ksp {
    arg("room.schemaLocation", "$projectDir/schemas")
}

dependencies {
    implementation(projects.core.model)

    implementation(libs.kotlinx.datetime)

    implementation(libs.room)
    implementation(libs.room.ktx)
    ksp(libs.room.ksp)
}