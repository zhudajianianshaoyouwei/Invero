package cc.trixey.invero.core.geneartor

import cc.trixey.invero.common.Invero
import cc.trixey.invero.common.supplier.ElementGenerator
import cc.trixey.invero.common.supplier.Object
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake

/**
 * Invero
 * cc.trixey.invero.core.geneartor.BaseGenerator
 *
 * @author Arasple
 * @since 2023/2/2 14:39
 */
abstract class BaseGenerator : ElementGenerator {

    override var generated: List<Object>? = null

    override fun filter(block: (Object) -> Boolean): ElementGenerator {
        generated = generated?.filter(block)
        return this
    }

    override fun <R : Comparable<R>> sortBy(block: (Object) -> R): ElementGenerator {
        generated = generated?.sortedBy(block)
        return this
    }

    fun register(name: String? = null) = Invero.api().registerElementGenerator(
        name = name ?: this.javaClass.simpleName.lowercase().removeSuffix("s"),
        provider = this.javaClass.getConstructor().newInstance()
    )

    companion object {

        @Awake(LifeCycle.INIT)
        internal fun default() = setOf(
            Custom(),
            Players(),
            Sounds(),
            Worlds()
        ).forEach { it.register() }

    }

}