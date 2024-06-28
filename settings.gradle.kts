pluginManagement {
    includeBuild("build-logic")

    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
        maven("https://artifactory-external.vkpartner.ru/artifactory/maven")
    }
}

rootProject.name = "BgituApp"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include(
    ":app",
    ":core:data",
    ":core:designsystem",
    ":feature:home",
    ":feature:login",
    ":core:datastore",
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
)
include(":core:data-test")
include(":core:datastore-test")
