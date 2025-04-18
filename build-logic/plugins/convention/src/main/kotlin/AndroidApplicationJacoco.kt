import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType
import ru.bgitu.buildlogic.configureJacoco

class AndroidApplicationJacoco : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("jacoco")
            val androidExtension = extensions.getByType<BaseAppModuleExtension>()

            androidExtension.buildTypes.configureEach {
                enableAndroidTestCoverage = true
                enableUnitTestCoverage = true
            }

            configureJacoco(extensions.getByType<ApplicationAndroidComponentsExtension>())
        }
    }
}
