
import com.android.build.gradle.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.project
import ru.bgitu.buildlogic.get
import ru.bgitu.buildlogic.implementation
import ru.bgitu.buildlogic.libs

class FeaturePlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        with(pluginManager) {
            apply("bgitu.library")
            apply("bgitu.library.compose")
            apply("bgitu.koin")
        }

        extensions.configure<LibraryExtension> {
            defaultConfig {
                testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                testOptions.animationsDisabled = true
            }
        }

        dependencies {
            implementation(project(":core:common"))
            implementation(project(":core:designsystem"))
            implementation(project(":core:model"))
            implementation(project(":core:data"))
            implementation(project(":core:model"))
            implementation(project(":core:domain"))
            implementation(project(":core:ui"))

            implementation(project(":core:navigation"))
            implementation(libs["kotlinx.coroutines.android"])
            implementation(libs["koin.compose"])
            implementation(libs["koin.navigation"])
            implementation(libs["lifecycle.compose"])
        }
    }
}