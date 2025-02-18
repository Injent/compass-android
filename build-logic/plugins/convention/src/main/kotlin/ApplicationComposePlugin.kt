
import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType
import ru.bgitu.buildlogic.configureCompose
import ru.bgitu.buildlogic.configureKotlin

class ApplicationComposePlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        with(pluginManager) {
            apply("com.android.application")
            apply("org.jetbrains.kotlin.plugin.compose")
        }
        configureKotlin(extensions.getByType<ApplicationExtension>())
        configureCompose(extensions.getByType<ApplicationExtension>())
    }
}