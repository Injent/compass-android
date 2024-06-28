import com.android.build.gradle.LibraryExtension
import ru.bgitu.buildlogic.configureCompose
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType

class LibraryComposePlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply("bgitu.library")
        configureCompose(extensions.getByType<LibraryExtension>())
    }
}