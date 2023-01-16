package cc.trixey.invero.core.util

/**
 * Invero
 * cc.trixey.invero.core.util.Regex
 *
 * @author Arasple
 * @since 2023/1/15 22:37
 */
val MATERIAL_ID = Regex("([0-9]+):?([0-9]+)?")

val PLACEHOLDER_ANY = Regex("[%{](.+)[%}]")

val SPECIAL_GROUP = "`(.+?)`".toPattern()

fun String.containsAnyPlaceholder(): Boolean {
    return contains(PLACEHOLDER_ANY)
}