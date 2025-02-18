
plugins {
    alias(libs.plugins.bgitu.library)
    alias(libs.plugins.bgitu.library.compose)
}

android.namespace = "ru.bgitu.core.designsystem"

dependencies {
    implementation(projects.core.common)

    implementation(libs.core.ktx)
    implementation(libs.coil.compose)

    api(libs.compose.activity)
    api(libs.compose.ui)
    api(libs.compose.runtime)
    api(libs.compose.material3)
    api(libs.compose.windowSizeClass)
    api(libs.compose.foundation)
    api(libs.compose.foundation.layout)
    api(libs.compose.ui.graphics)
    api(libs.compose.ui.text)
    api(libs.compose.ui.util)
    api(libs.compose.ui.tooling.preview)
    api(libs.compose.constraintLayout)
    api(libs.compose.adaptive)
    api(libs.compose.navigation)
    api(libs.infoBarCompose)
    debugImplementation(libs.compose.ui.tooling)
}