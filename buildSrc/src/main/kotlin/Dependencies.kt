import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.plugins.PluginAware
import org.gradle.kotlin.dsl.*

val rootName = "Invero"
val rootGroup = "cc.trixey.invero"
val rootVersion = "1.0.0-dev-7"

val kotlinVersion = "1.8.0"
val shadowJarVersion = "7.1.2"
val taboolibVersion = "6.0.10-70"
val taboolibPluginVersion = "1.56"

val repoTabooProject = "https://repo.tabooproject.org/repository/releases"

fun PluginAware.applyPlugins() {
    apply(plugin = "maven-publish")
    apply(plugin = "com.github.johnrengelman.shadow")
    apply(plugin = "org.jetbrains.kotlin.jvm")
}

fun Project.initSubProject(publish: Project.() -> Unit) {
    if (parent?.name != "plugin") {
        @Suppress("DEPRECATION")
        gradle.buildFinished { buildDir.deleteRecursively() }
    }
    if (parent != rootProject && name.startsWith("framework")) {
        publish()
    }
}

fun RepositoryHandler.projectRepositories() {
    maven(repoTabooProject)
    mavenCentral()
}