package cc.trixey.invero.common.supplier

/**
 * Invero
 * cc.trixey.invero.common.ObjectElement
 *
 * @author Arasple
 * @since 2023/2/1 16:46
 */
class Object(val content: Map<String, String>) {

    operator fun get(key: String): String? {
        return content[key]
    }

    val variables: Map<String, Any> by lazy {
        content
            .map { "element_${it.key}" to it.value }
            .toMap() + mapOf("@element" to this)
    }

}

inline fun sourceObject(block: MutableMap<String, Any?>.() -> Unit): Object = mutableMapOf<String, Any?>()
    .let { mutableMap ->
        block(mutableMap)
        return mutableMap
            .map { entry -> entry.key to entry.value.toString() }
            .toMap()
            .let { Object(it) }
    }