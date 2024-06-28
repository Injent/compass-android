
import com.android.build.api.dsl.ApplicationExtension
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import ru.bgitu.buildlogic.configureFlavors
import ru.bgitu.buildlogic.configureKotlin
import ru.bgitu.buildlogic.libs

class ApplicationPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        with(pluginManager) {
            apply("com.android.application")
            apply("org.jetbrains.kotlin.android")
            apply("io.gitlab.arturbosch.detekt")
        }

        extensions.configure<DetektExtension> {
            toolVersion = libs.findVersion("detekt").toString()

            config.setFrom(file("config/detekt/detekt.yml"))
            buildUponDefaultConfig = true
        }

        extensions.configure<ApplicationExtension> {
            configureKotlin(this)
            defaultConfig.targetSdk = libs.findVersion("targetSdk").get().toString().toInt()
            configureFlavors(this)
        }
    }
}