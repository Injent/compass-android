package ru.bgitu.buildlogic

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.io.File

internal fun Project.configureCompose(
    commonExtension: CommonExtension<*, *, *, *, *, *>
) {
    commonExtension.apply {
        buildFeatures {
            compose = true
        }

        dependencies {
            val bom = libs["compose.bom"]
            implementation(platform(bom))
            add("androidTestImplementation", platform(bom))
        }

        tasks.withType<KotlinCompile>().configureEach {
            compilerOptions {
                freeCompilerArgs.apply {
                    addAll(buildComposeMetricsParameters())
                    addAll(buildComposeStabilityConfig())
                }
            }
        }
    }
}

private fun Project.buildComposeStabilityConfig(): List<String> {
    val stabilityConfig = File(rootDir, "config/compose/stability.conf")
    val params = mutableListOf<String>()
    if (stabilityConfig.exists()) {
        params.add("-P")
        params.add("plugin:androidx.compose.compiler.plugins.kotlin:stabilityConfigurationPath=" + stabilityConfig.absolutePath)
    }

    return params
}

private fun Project.buildComposeMetricsParameters(): List<String> {
    val buildDir = layout.buildDirectory.asFile.get()
    val metricParameters = mutableListOf<String>()

    val metricsFolder = File(buildDir, "compose-metrics")
    metricParameters.add("-P")
    metricParameters.add(
        "plugin:androidx.compose.compiler.plugins.kotlin:metricsDestination=" + metricsFolder.absolutePath
    )

    val reportsFolder = File(buildDir, "compose-reports")
    metricParameters.add("-P")
    metricParameters.add(
        "plugin:androidx.compose.compiler.plugins.kotlin:reportsDestination=" + reportsFolder.absolutePath
    )
    return metricParameters.toList()
}