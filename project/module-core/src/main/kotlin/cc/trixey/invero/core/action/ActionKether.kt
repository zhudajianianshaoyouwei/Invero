package cc.trixey.invero.core.action

import cc.trixey.invero.core.Context
import cc.trixey.invero.core.serialize.ActionKetherSerializer
import cc.trixey.invero.core.util.KetherHandler
import cc.trixey.invero.core.util.bool
import kotlinx.serialization.Serializable
import taboolib.common.platform.function.submitAsync
import java.util.concurrent.CompletableFuture

/**
 * Invero
 * cc.trixey.invero.core.action.ActionKether
 *
 * @author Arasple
 * @since 2023/1/18 11:30
 */
@Serializable(with = ActionKetherSerializer::class)
class ActionKether(val script: String) : Action() {

    override fun run(context: Context): CompletableFuture<Boolean> {
        val player = context.player
        val scripts = script.split("\\n")
        val variables = context.variables

        if (scripts.size <= 1) {
            return KetherHandler.invoke(script, player, variables).thenApply { it.bool }
        } else {
            submitAsync {
                for (index in 0 until scripts.lastIndex) {
                    val script = scripts[index]
                    KetherHandler.invoke(script, player, variables).thenApply { it.bool }.get()
                }
            }
            return KetherHandler.invoke(scripts.last(), player, variables).thenApply { it.bool }
        }
    }

}