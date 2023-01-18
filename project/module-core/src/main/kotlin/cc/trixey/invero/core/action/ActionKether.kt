package cc.trixey.invero.core.action

import cc.trixey.invero.core.Context
import cc.trixey.invero.core.serialize.ActionKetherSerializer
import cc.trixey.invero.core.util.KetherHandler
import cc.trixey.invero.core.util.bool
import kotlinx.serialization.Serializable
import taboolib.common.platform.function.isPrimaryThread
import taboolib.common.platform.function.submit
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

        if (scripts.size <= 1) {
            return KetherHandler.invoke(script, player, context.variables).thenApply { it.bool }
        } else {
            val future = CompletableFuture<Boolean>()
            submit(async = !isPrimaryThread) {
                scripts.forEachIndexed { index, script ->
                    val result = KetherHandler.invoke(script, player, context.variables).thenApply { it.bool }.get()
                    if (!result) {
                        future.complete(false)
                        return@forEachIndexed
                    } else if (index == scripts.lastIndex) {
                        future.complete(true)
                    }
                }
            }
            return future
        }
    }

}