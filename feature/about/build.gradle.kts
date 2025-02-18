plugins {
    alias(libs.plugins.bgitu.feature)
}

android.namespace = "ru.bgitu.feature.about"

dependencies {
    implementation(libs.markdown)
    implementation(libs.lazyPaginationCompose)
    implementation(libs.konfetti)
}