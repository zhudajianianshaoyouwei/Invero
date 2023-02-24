package cc.trixey.invero.core.geneartor

import cc.trixey.invero.common.supplier.sourceObject
import cc.trixey.invero.core.Context
import org.bukkit.entity.Player

/**
 * Invero
 * cc.trixey.invero.core.geneartor.Debug
 *
 * @author Arasple
 * @since 2023/2/24 15:20
 */
class PlayerBGenerator : ContextGenerator() {

    override fun generate(context: Context) {
        context.variables.forEach {
            println("[${it.key}] ${it.value} -- (${it.value.javaClass.simpleName})")
        }

        val player = context.variables["player_instance"] as Player

        generated = arrayOf("name").map {
            sourceObject {
                put(it, player.name)
            }
        }
    }

}