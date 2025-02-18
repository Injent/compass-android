import java.util.Properties

plugins {
    alias(libs.plugins.bgitu.app)
    alias(libs.plugins.bgitu.app.compose)
    alias(libs.plugins.bgitu.koin)
    alias(libs.plugins.firebase.crashlytics)
    alias(libs.plugins.google.services)
    alias(libs.plugins.huaweiAgConnect)
    alias(libs.plugins.secrets)
    alias(libs.plugins.bgitu.detekt)
    alias(libs.plugins.bgitu.serialization)
}

android {
    namespace = "ru.bgitu.app"

    defaultConfig {
        applicationId = "ru.bgitu.app"
        versionCode = 16
        versionName = "1.3-release"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
        buildFeatures.buildConfig = true

        val props = Properties().apply {
            rootProject.file("secrets.properties").inputStream().use { load(it) }
        }

        addManifestPlaceholders(
            mapOf(
                "VKIDClientID" to props.getProperty("VKIDClientID"),
                "VKIDClientSecret" to props.getProperty("VKIDClientSecret"),
                "VKIDRedirectHost" to "vk.com",
                "VKIDRedirectScheme" to "vk${props.getProperty("VKIDClientID")}",
            )
        )

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
            storeFile = project.file(props.getProperty("storeFile"))
            storePassword = props.getProperty("storePassword")
            keyAlias = props.getProperty("keyAlias")
            keyPassword = props.getProperty("keyPassword")
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            isCrunchPngs = false
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
            signingConfig = signingConfigs.getByName("release")
        }
    }
}

agcp {
    manifest = false
}

dependencies {
    // Features
    implementation(projects.feature.home)
    implementation(projects.feature.profile)
    implementation(projects.feature.update)
    implementation(projects.feature.settings)
    implementation(projects.feature.about)
    implementation(projects.feature.help)
    implementation(projects.feature.scheduleNotifier.api)
    implementation(projects.feature.scheduleNotifier.impl)
    implementation(projects.feature.scheduleWidget)
    implementation(projects.feature.professorSearch)
    implementation(projects.feature.groups)
    implementation(projects.feature.input)

    implementation(projects.core.designsystem)
    implementation(projects.core.network)
    implementation(projects.core.data)
    implementation(projects.core.common)
    implementation(projects.core.domain)
    implementation(projects.core.datastore)
    implementation(projects.core.model)
    implementation(projects.core.navigation)
    implementation(projects.core.notifications)
    implementation(projects.core.ui)

    implementation(projects.components.updates.api)
    implementation(projects.components.updates.impl)
    implementation(projects.components.sync)

    // Core
    implementation(libs.compose.activity)
    implementation(libs.core.ktx)

    // Coil
    implementation(libs.coil.compose)
    implementation(libs.coil.network.ktor3)

    // Lifecycle
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.lifecycle.compose)

    implementation(libs.koin.compose)
    implementation(libs.splashScreen)

    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.analytics)

    implementation(libs.startup)
    implementation(libs.kotlinx.datetime)

    huaweiImplementation(libs.agconnect.core)
    huaweiImplementation(libs.huawei.hianalytics)
    huaweiImplementation(libs.huawei.crash)

    rustoreImplementation(platform(libs.rustore.bom))
    rustoreImplementation(libs.rustore.push)
}