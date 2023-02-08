import org.gradle.api.file.FileTreeElement

private val excludes = setOf(
    "META-INF",
)

fun FileTreeElement.shouldExclude(): Boolean {
    return isDirectory && file.name in excludes
}