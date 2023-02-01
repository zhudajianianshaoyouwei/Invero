package cc.trixey.invero.core.geneartor

import cc.trixey.invero.common.Invero
import cc.trixey.invero.common.supplier.ElementGenerator
import cc.trixey.invero.common.supplier.Object
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake

/**
 * Invero
 * cc.trixey.invero.core.geneartor.Custom
 *
 * @author Arasple
 * @since 2023/1/29 22:30
 */
internal class Custom : ElementGenerator {

    override var generated: List<Object>? = listOf()

    override fun generate(): List<Object> {
        return generated!!
    }

    override fun filter(block: (Object) -> Boolean): ElementGenerator {
        generated = generated?.filter(block)
        return this
    }

    override fun <R : Comparable<R>> sortBy(block: (Object) -> R): ElementGenerator {
        generated = generated?.sortedBy(block)
        return this
    }

    companion object {

        @Awake(LifeCycle.INIT)
        fun init() {
            Invero.api().registerElementGenerator("custom", Custom())
        }

    }

}