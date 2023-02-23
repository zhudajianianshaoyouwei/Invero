package cc.trixey.invero.core.geneartor

import cc.trixey.invero.common.Invero
import cc.trixey.invero.common.supplier.ElementGenerator
import cc.trixey.invero.common.supplier.Object
import cc.trixey.invero.core.Context

/**
 * Invero
 * cc.trixey.invero.core.geneartor.ContextGenerator
 *
 * @author Arasple
 * @since 2023/2/23 9:27
 */
abstract class ContextGenerator : ElementGenerator {

    override var generated: List<Object>? = null

    override fun generate(context: Any?) = this.generate(context as Context)

    abstract fun generate(context: Context)

    override fun filter(block: (Object) -> Boolean): ElementGenerator {
        generated = generated?.filter(block)
        return this
    }

    override fun <R : Comparable<R>> sortBy(block: (Object) -> R): ElementGenerator {
        generated = generated?.sortedBy(block)
        return this
    }

    fun register(name: String? = null) = Invero.API.registerElementGenerator(
        name = name ?: this.javaClass.simpleName.lowercase().removeSuffix("s"),
        provider = this.javaClass.getConstructor().newInstance()
    )

}