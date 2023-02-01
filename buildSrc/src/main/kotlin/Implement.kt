import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.kotlin.dsl.project


fun DependencyHandler.`compileLocal`(project: Project, vararg dir: String) {
    dir.forEach { add("compileOnly", project.fileTree(it)) }
}

fun DependencyHandler.`compileModule`(vararg name: String) {
    name.forEach { add("compileOnly", project(":project:$it")) }
}

fun DependencyHandler.`implementateModule`(vararg name: String) {
    name.forEach { add("implementation", project(":project:$it")) }
}

fun DependencyHandler.`compileNMS`() {
    add("compileOnly", "ink.ptms:nms-all:1.0.0")
}

fun DependencyHandler.`compileCore`(
    version: Int,
    minimize: Boolean = true,
    mapped: Boolean = false,
    complete: Boolean = false,
) {
    val notation =
        "ink.ptms.core:v$version:$version${if (!complete && minimize) "-minimize" else ""}${if (complete) "" else if (mapped) ":mapped" else ":universal"}"
    add("compileOnly", notation)
}

fun DependencyHandler.installTaboo(vararg module: String) = module.forEach {
    add("compileOnly", "io.izzel.taboolib:$it:$taboolibVersion")
}

fun DependencyHandler.implementateTaboo(vararg module: String) = module.forEach {
    add("implementation", "io.izzel.taboolib:$it:$taboolibVersion")
}