
import com.android.build.gradle.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin
import ru.bgitu.buildlogic.configureFlavors
import ru.bgitu.buildlogic.configureKotlin
import ru.bgitu.buildlogic.libs
import ru.bgitu.buildlogic.testImplementation

class LibraryPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        with(pluginManager) {
            apply("com.android.library")
            apply("org.jetbrains.kotlin.android")
            apply("io.gitlab.arturbosch.detekt")
        }

        extensions.configure<LibraryExtension> {
            configureKotlin(this)
            defaultConfig.targetSdk = libs.findVersion("targetSdk").get().toString().toInt()
            testOptions.animationsDisabled = true
            configureFlavors(this)

            defaultConfig {
                consumerProguardFiles("consumer-proguard-rules.pro")
            }

            sourceSets.getByName("demo").assets.srcDirs(files("$projectDir/sampledata"))
        }

        dependencies {
            testImplementation(kotlin("test"))
        }
    }
}