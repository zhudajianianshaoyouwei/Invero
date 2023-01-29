package cc.trixey.invero.core.geneartor

/**
 * Invero
 * cc.trixey.invero.core.geneartor.Object
 *
 * @author Arasple
 * @since 2023/1/29 22:05
 */
@JvmInline
value class Object(val content: Map<String, String>) {

    operator fun get(key: String): String? {
        return content[key]
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