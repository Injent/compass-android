import com.android.build.gradle.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType
import ru.bgitu.buildlogic.configureVkPlaceholder

class VKPlaceholders : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        configureVkPlaceholder(extensions.getByType<LibraryExtension>())
    }
}