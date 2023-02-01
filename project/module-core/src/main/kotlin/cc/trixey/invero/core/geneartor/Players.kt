package cc.trixey.invero.core.geneartor

import cc.trixey.invero.common.Invero
import cc.trixey.invero.common.supplier.ElementGenerator
import cc.trixey.invero.common.supplier.Object
import cc.trixey.invero.common.supplier.sourceObject
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.platform.util.onlinePlayers

/**
 * Invero
 * cc.trixey.invero.core.geneartor.Players
 *
 * @author Arasple
 * @since 2023/1/29 22:03
 */
internal class Players : ElementGenerator {

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

    override fun filter(block: (Object) -> Boolean): Players {
        generated = generated?.filter(block)
        return this
    }

    override fun <R : Comparable<R>> sortBy(block: (Object) -> R): Players {
        generated = generated?.sortedBy(block)
        return this
    }

    companion object {

        @Awake(LifeCycle.INIT)
        fun init() {
            Invero.api().registerElementGenerator("player", Players())
        }

    }

}