package ru.bgitu.buildlogic

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import java.util.Properties

internal fun Project.configureVkPlaceholder(
    commonExtension: CommonExtension<*, *, *, *, *, *>
) {
    val props = Properties().apply {
        rootProject.file("secrets.properties").bufferedReader().use(::load)
    }
    commonExtension.defaultConfig.addManifestPlaceholders(
        mapOf(
            "VKIDClientID" to props.getProperty("VKIDClientID"),
            "VKIDClientSecret" to props.getProperty("VKIDClientSecret"),
            "VKIDRedirectHost" to "vk.com",
            "VKIDRedirectScheme" to "vk${props.getProperty("VKIDClientID")}",
        )
    )
}