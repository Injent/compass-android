import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
}

group = "ru.bgitu.buildlogic"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.ksp.gradlePlugin)
    compileOnly(libs.detekt.gradlePlugin)
}

tasks {
    validatePlugins {
        enableStricterValidation = true
        failOnWarning = true
    }
}

gradlePlugin {
    plugins {
        register("application") {
            id = "bgitu.app"
            implementationClass = "ApplicationPlugin"
        }
        register("applicationCompose") {
            id = "bgitu.app.compose"
            implementationClass = "ApplicationComposePlugin"
        }
        register("library") {
            id = "bgitu.library"
            implementationClass = "LibraryPlugin"
        }
        register("libraryCompose") {
            id = "bgitu.library.compose"
            implementationClass = "LibraryComposePlugin"
        }
        register("feature") {
            id = "bgitu.feature"
            implementationClass = "FeaturePlugin"
        }
        register("kotlinSerialization") {
            id = "bgitu.serialization"
            implementationClass = "SerializationPlugin"
        }
        register("koin") {
            id = "bgitu.koin"
            implementationClass = "KoinAndroidPlugin"
        }
        register("detekt") {
            id = "bgitu.detekt"
            implementationClass = "DetektPlugin"
        }
        register("vkPlaceholders") {
            id = "bgitu.vkplaceholders"
            implementationClass = "VKPlaceholders"
        }
        register("androidTest") {
            id = "bgitu.android.test"
            implementationClass = "AndroidTest"
        }
        register("appAndroidJacoco") {
            id = "bgitu.app.jacoco"
            implementationClass = "AndroidApplicationJacoco"
        }
        register("libAndroidJacoco") {
            id = "bgitu.library.jacoco"
            implementationClass = "AndroidLibraryJacoco"
        }
    }
}