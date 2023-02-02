package cc.trixey.invero.core.geneartor

import cc.trixey.invero.common.supplier.Object

/**
 * Invero
 * cc.trixey.invero.core.geneartor.Custom
 *
 * @author Arasple
 * @since 2023/1/29 22:30
 */
class Custom : BaseGenerator() {

    override var generated: List<Object>? = listOf()

    override fun generate(): List<Object> {
        return generated!!
    }

}