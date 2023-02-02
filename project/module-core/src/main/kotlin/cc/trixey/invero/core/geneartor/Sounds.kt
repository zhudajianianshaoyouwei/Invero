package cc.trixey.invero.core.geneartor

import cc.trixey.invero.common.supplier.Object
import cc.trixey.invero.common.supplier.sourceObject
import org.bukkit.Sound

/**
 * Invero
 * cc.trixey.invero.core.geneartor.Sounds
 *
 * @author Arasple
 * @since 2023/2/2 14:34
 */
class Sounds : BaseGenerator() {

    override fun generate(): List<Object> {
        generated = Sound.values().map {
            sourceObject {
                put("name", it.name)
                put("ordinal", it.ordinal)
            }
        }
        return generated!!
    }

}