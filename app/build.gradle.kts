import java.util.Properties

plugins {
    alias(libs.plugins.bgitu.app)
    alias(libs.plugins.bgitu.app.compose)
    alias(libs.plugins.bgitu.koin)
    alias(libs.plugins.firebase.crashlytics)
    alias(libs.plugins.google.services)
    alias(libs.plugins.secrets)
    alias(libs.plugins.bgitu.detekt)
    alias(libs.plugins.bgitu.serialization)
    alias(libs.plugins.bgitu.app.jacoco)
    alias(libs.plugins.moduleDependencyGraph)
}

android {
    namespace = "ru.bgitu.app"

    defaultConfig {
        applicationId = "ru.bgitu.app"
        targetSdk = 34
        versionCode = 9
        versionName = "dev"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
        buildFeatures.buildConfig = true

        resourceConfigurations += setOf(
            "en",
            "ru",
            "af",
            "ar",
            "be",
            "bn",
            "cs",
            "da",
            "de",
            "es",
            "eu",
            "fa",
            "fil",
            "fr",
            "hi",
            "hu",
            "ia",
            "in",
            "it",
            "iw",
            "ja",
            "kk",
            "kn",
            "ko",
            "nl",
            "pl",
            "pt",
            "pt-rBR",
            "ro",
            "sk",
            "sr",
            "te",
            "th",
            "tr",
            "uk",
            "vi",
            "zh-rCN",
            "zh-rTW"
        )
    }

    signingConfigs {
        register("release") {
            val props = Properties().apply {
                file("release_config.properties").inputStream().use { load(it) }
            }
            storeFile = file(props.getProperty("storeFile"))
            storePassword = props.getProperty("storePassword")
            keyAlias = props.getProperty("keyAlias")
            keyPassword = props.getProperty("keyPassword")
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            manifestPlaceholders["crashlyticsCollectionEnabled"] = true
            signingConfig = signingConfigs.getByName("release")
        }
        debug {
            isMinifyEnabled = false
            manifestPlaceholders["crashlyticsCollectionEnabled"] = false
        }
    }
}

dependencies {
    // Features
    implementation(projects.feature.home)
    implementation(projects.feature.findmate)
    implementation(projects.feature.profile)
    implementation(projects.feature.login)
    implementation(projects.feature.update)
    implementation(projects.feature.settings)
    implementation(projects.feature.profileSettings)
    implementation(projects.feature.about)
    implementation(projects.feature.help)
    implementation(projects.feature.scheduleNotifier.api)
    implementation(projects.feature.scheduleNotifier.impl)
    implementation(projects.feature.scheduleWidget)
    implementation(projects.feature.professorSearch)
    implementation(projects.feature.groups)

    implementation(projects.core.designsystem)
    implementation(projects.core.network)
    implementation(projects.core.data)
    implementation(projects.core.common)
    implementation(projects.core.domain)
    implementation(projects.core.datastore)
    implementation(projects.core.model)
    implementation(projects.core.navigation)
    implementation(projects.core.database)
    implementation(projects.core.notifications)
    implementation(projects.core.ui)

    implementation(projects.components.updates.api)
    implementation(projects.components.updates.impl)
    implementation(projects.components.sync)

    // Core
    implementation(libs.compose.activity)
    implementation(libs.core.ktx)
    implementation(libs.coil)

    // Lifecycle
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.lifecycle.compose)

    implementation(libs.koin.workManager)
    implementation(libs.koin.compose)
    implementation(libs.splashScreen)

    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.messaging)
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.analytics)

    implementation(libs.startup)
    implementation(libs.kotlinx.datetime)

    // Rustore
    rustoreImplementation(libs.rustore.core)
    rustoreImplementation(libs.rustore.analytics)
}