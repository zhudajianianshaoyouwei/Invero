package cc.trixey.invero.core.geneartor.source

import cc.trixey.invero.core.geneartor.Generator
import cc.trixey.invero.core.geneartor.Object
import cc.trixey.invero.core.geneartor.sourceObject
import taboolib.platform.util.onlinePlayers

/**
 * Invero
 * cc.trixey.invero.core.geneartor.source.PlayerGenerator
 *
 * @author Arasple
 * @since 2023/1/29 22:03
 */
class PlayerGenerator : Generator {

    override var generated: List<Object>? = null

    override fun generate(): List<Object> {
        generated = onlinePlayers.map {
            sourceObject {
                put("name", it.name)
                put("displayName", it.displayName)
                put("isSneaking", it.isSneaking)
                put("isSprinting", it.isSprinting)
                put("x", it.location.x)
                put("y", it.location.y)
                put("z", it.location.z)
                put("yaw", it.location.yaw)
                put("pitch", it.location.pitch)
                put("address", it.address?.hostString)
            }
        }
        return generated!!
    }

    override fun filter(block: (Object) -> Boolean): PlayerGenerator {
        generated = generated?.filter(block)
        return this
    }

    override fun <R : Comparable<R>> sort(block: (Object) -> R): PlayerGenerator {
        generated = generated?.sortedBy(block)
        return this
    }

    override fun isEmpty(): Boolean {
        return generated.isNullOrEmpty()
    }

}