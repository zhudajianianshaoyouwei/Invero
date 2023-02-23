package cc.trixey.invero.core.geneartor

import cc.trixey.invero.common.supplier.sourceObject
import org.bukkit.Sound

/**
 * Invero
 * cc.trixey.invero.core.geneartor.GeneratorSounds
 *
 * @author Arasple
 * @since 2023/2/2 14:34
 */
class GeneratorSounds : BaseGenerator() {

    override fun generate() {
        generated = Sound.values().map {
            sourceObject {
                put("name", it.name)
                put("ordinal", it.ordinal)
            }
        }
    }

}