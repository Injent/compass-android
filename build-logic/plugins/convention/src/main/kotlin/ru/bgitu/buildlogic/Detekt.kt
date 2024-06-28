package ru.bgitu.buildlogic

import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.Project

fun Project.configureDetekt(
    extension: DetektExtension
) {
    extension.apply {
        toolVersion = libs.findVersion("detekt").toString()
        config.setFrom(file("config/detekt/detekt.yml"))
        buildUponDefaultConfig = true
        source.from(files("src/main/kotlin/"))
    }
}