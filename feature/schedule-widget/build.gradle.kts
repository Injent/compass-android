
plugins {
    alias(libs.plugins.bgitu.library)
    alias(libs.plugins.bgitu.library.compose)
    alias(libs.plugins.bgitu.koin)
    alias(libs.plugins.bgitu.serialization)
    alias(libs.plugins.protobuf)
    alias(libs.plugins.secrets)
}

android {
    namespace = "ru.bgitu.feature.schedule_widget"
    defaultConfig {
        consumerProguardFiles("consumer-proguard-rules.pro")
    }
    buildFeatures.buildConfig = true
}

protobuf {
    protoc {
        artifact = libs.protobuf.protoc.get().toString()
    }
    generateProtoTasks {
        all().forEach { task ->
            task.builtins {
                val java by registering {
                    option("lite")
                }
                val kotlin by registering {
                    option("lite")
                }
            }
        }
    }
}

dependencies {
    implementation(projects.core.data)
    implementation(projects.core.datastore)
    implementation(projects.core.common)
    implementation(projects.core.model)
    implementation(projects.core.designsystem)
    implementation(projects.core.notifications)

    implementation(libs.glance)
    implementation(libs.glance.widget)
    implementation(libs.kotlinx.datetime)
    implementation(libs.work)

    implementation(libs.protobuf.kotlin.lite)
    implementation(libs.protobuf.protoc)
}