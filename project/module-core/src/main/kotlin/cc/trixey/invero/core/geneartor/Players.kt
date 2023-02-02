package cc.trixey.invero.core.geneartor

import cc.trixey.invero.common.supplier.Object
import cc.trixey.invero.common.supplier.sourceObject
import taboolib.platform.util.onlinePlayers

/**
 * Invero
 * cc.trixey.invero.core.geneartor.Players
 *
 * @author Arasple
 * @since 2023/1/29 22:03
 */
class Players : BaseGenerator() {

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

}