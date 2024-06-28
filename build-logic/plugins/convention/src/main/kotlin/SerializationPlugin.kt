import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import ru.bgitu.buildlogic.get
import ru.bgitu.buildlogic.implementation
import ru.bgitu.buildlogic.libs

class SerializationPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply("org.jetbrains.kotlin.plugin.serialization")

        dependencies {
            implementation(libs["kotlinx.serialization"])
        }
    }
}