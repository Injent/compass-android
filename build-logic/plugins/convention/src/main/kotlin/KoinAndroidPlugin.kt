import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import ru.bgitu.buildlogic.get
import ru.bgitu.buildlogic.implementation
import ru.bgitu.buildlogic.libs

class KoinAndroidPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        dependencies {
            implementation(platform(libs["koin-bom"]))
            implementation(libs["koin-android"])
        }
    }
}