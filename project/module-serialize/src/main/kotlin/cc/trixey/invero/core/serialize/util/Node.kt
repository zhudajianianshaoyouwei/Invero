package cc.trixey.invero.core.serialize.util

import cc.trixey.invero.core.serialize.TolorantNode
import cc.trixey.invero.core.serialize.TolorantType
import taboolib.library.configuration.ConfigurationSection

/**
 * Invero
 * cc.trixey.invero.core.serialize.util.Node
 *
 * @author Arasple
 * @since 2023/1/15 14:29
 */
fun tolerantRegex(vararg key: String): Regex {
    return Regex("(?i)(${key.joinToString("|")})")
}

fun ConfigurationSection.getTolerant(path: String, vararg type: TolorantType): Pair<TolorantType, Any?>? {
    return TolorantNode("(?i)$path".toRegex(), type.toList()).get(this)
}

fun ConfigurationSection.getTolerantKey(path: String): String? {
    return TolorantNode("(?i)$path".toRegex()).findKey(this)
}

fun Any?.castSection(): ConfigurationSection {
    return this as ConfigurationSection
}

fun Any?.castList(): List<*> {
    return this as List<*>
}