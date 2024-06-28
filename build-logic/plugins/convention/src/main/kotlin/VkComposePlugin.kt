import com.vk.gradle.plugin.compose.utils.VkomposeExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import java.io.File

class VkComposePlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        extensions.configure<VkomposeExtension> {
            skippabilityCheck {
                stabilityConfigurationPath = File(rootDir, "config/compose/stability.conf").absolutePath
            }

            recompose {
                isHighlighterEnabled = true
                isLoggerEnabled = true
            }

            sourceInformationClean = true
        }
    }
}