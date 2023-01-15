package cc.trixey.invero.core.regex

/**
 * Invero
 * cc.trixey.invero.core.regex.Item
 *
 * @author Arasple
 * @since 2023/1/15 11:55
 */
val MATERIAL_ID = Regex("([0-9]+):?([0-9]+)?")

val PLACEHOLDER_ANY = Regex("[%{](.+)[%}]")

fun String.containsAnyPlaceholder(): Boolean {
    return contains(PLACEHOLDER_ANY)
}