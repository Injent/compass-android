pluginManagement {
    includeBuild("build-logic")

    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven("https://developer.huawei.com/repo")
        maven("https://artifactory-external.vkpartner.ru/artifactory/vkid-sdk-android/")
    }

    resolutionStrategy {
        eachPlugin {
            if (requested.id.id == "com.huawei.agconnect") {
                useModule("com.huawei.agconnect:agcp:1.9.1.300")
            }
        }
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
        maven(url = "https://artifactory-external.vkpartner.ru/artifactory/vkid-sdk-android/")
        maven(url = "https://artifactory-external.vkpartner.ru/artifactory/maven")
    }
}

rootProject.name = "BgituApp"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include(
    ":app",
    ":core:data",
    ":core:data-test",
    ":core:designsystem",
    ":feature:home",
    ":feature:login",
    ":core:datastore",
    ":core:datastore-test",
    ":core:network",
    ":core:model",
    ":core:domain",
    ":core:common",
    ":core:navigation",
    ":feature:profile",
    ":core:ui",
    ":core:testing",
    ":core:database",
    ":components:sync",
    ":components:signin",
    ":core:notifications",
    ":components:updates:api",
    ":components:updates:impl",
    ":feature:update",
    ":feature:settings",
    ":feature:schedule-widget",
    ":feature:profile-settings",
    ":feature:about",
    ":feature:help",
    ":feature:schedule-notifier:api",
    ":feature:schedule-notifier:impl",
    ":feature:professor-search",
    ":feature:findmate",
    ":feature:groups",
    ":feature:input"
)
include(":feature:onboarding")
