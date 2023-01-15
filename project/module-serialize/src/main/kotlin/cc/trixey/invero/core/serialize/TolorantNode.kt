package cc.trixey.invero.core.serialize

import cc.trixey.invero.core.serialize.TolorantType.*
import taboolib.library.configuration.ConfigurationSection


/**
 * Invero
 * cc.trixey.invero.core.serialize.TolorantNode
 *
 * @author Arasple
 * @since 2023/1/15 14:36
 */
class TolorantNode(val node: Regex, val acceptable: List<TolorantType> = listOf()) {

    fun findKey(section: ConfigurationSection): String? {
        return section.getKeys(false).find { node.matches(it) }
    }

    fun get(section: ConfigurationSection): Pair<TolorantType, Any?>? {
        val path = findKey(section) ?: error("Not found available path of $node")

        acceptable.forEach { type ->

            when (type) {
                STRING -> {
                    if (section.isString(path)) {
                        return STRING to section.getString(path)
                    }
                }

                INT -> if (section.isInt(path)) {
                    return INT to section.getInt(path)
                }

                BOOLEAN -> if (section.isBoolean(path)) {
                    return BOOLEAN to section.getBoolean(path)
                }

                LIST_OBJECT -> if (section.isList(path)) {
                    return LIST_OBJECT to section.getList(path)
                }

                LIST_STRING -> if (section.isList(path) && section.getList(path)?.all { it is String } == true) {
                    return LIST_STRING to section.getStringList(path)
                }

                LIST_INT -> if (section.isList(path)) {
                    val list = section.getList(path)
                    if (list?.firstOrNull()?.toString()?.toIntOrNull() != null) {
                        return LIST_INT to section.getIntegerList(path)
                    }
                }

                SECTION -> if (section.isConfigurationSection(path)) {
                    return SECTION to section[path]
                }
            }

        }

        return null
    }

}