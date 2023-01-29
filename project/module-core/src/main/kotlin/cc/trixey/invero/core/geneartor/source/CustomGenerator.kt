package cc.trixey.invero.core.geneartor.source

import cc.trixey.invero.core.geneartor.Generator
import cc.trixey.invero.core.geneartor.Object

/**
 * Invero
 * cc.trixey.invero.core.geneartor.source.CustomGenerator
 *
 * @author Arasple
 * @since 2023/1/29 22:30
 */
class CustomGenerator(custom: List<Object> = emptyList()) : Generator {

    override var generated: List<Object>? = custom

    override fun generate(): List<Object> {
        return generated!!
    }

    override fun filter(block: (Object) -> Boolean): Generator {
        generated = generated?.filter(block)
        return this
    }

    override fun <R : Comparable<R>> sort(block: (Object) -> R): Generator {
        generated = generated?.sortedBy(block)
        return this
    }

    override fun isEmpty(): Boolean {
        return generated.isNullOrEmpty()
    }

}