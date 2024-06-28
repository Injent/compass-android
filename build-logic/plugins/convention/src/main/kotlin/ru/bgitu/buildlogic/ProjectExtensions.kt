package ru.bgitu.buildlogic

import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.kotlin.dsl.getByType

val Project.libs
    get(): VersionCatalog = extensions.getByType<VersionCatalogsExtension>().named("libs")

operator fun VersionCatalog.get(alias: String) = this.findLibrary(alias).get()

internal fun DependencyHandlerScope.implementation(dependency: Any) {
    add("implementation", dependency)
}

internal fun DependencyHandlerScope.testImplementation(dependency: Any) {
    add("testImplementation", dependency)
}

internal fun DependencyHandlerScope.androidTestImplementation(dependency: Any) {
    add("androidTestImplementation", dependency)
}

internal fun DependencyHandlerScope.ksp(dependency: Any) {
    add("ksp", dependency)
}