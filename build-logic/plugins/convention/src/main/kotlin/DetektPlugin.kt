
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import ru.bgitu.buildlogic.configureDetekt

class DetektPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        extensions.configure<DetektExtension> {
            configureDetekt(this)
        }
    }
}