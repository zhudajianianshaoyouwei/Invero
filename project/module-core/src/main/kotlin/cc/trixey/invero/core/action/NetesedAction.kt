package cc.trixey.invero.core.action

import cc.trixey.invero.core.Context
import cc.trixey.invero.core.serialize.NetesedActionSerializer
import kotlinx.serialization.Serializable
import taboolib.common.platform.function.isPrimaryThread
import taboolib.common.platform.function.submit
import java.util.concurrent.CompletableFuture

/**
 * Invero
 * cc.trixey.invero.core.action.NetesedAction
 *
 * @author Arasple
 * @since 2023/1/18 12:14
 */
@Serializable(with = NetesedActionSerializer::class)
class NetesedAction(val actions: List<Action>) : Action() {

    override fun run(context: Context): CompletableFuture<Boolean> {
        val future = CompletableFuture<Boolean>()
        submit(async = !isPrimaryThread) {
            for (index in actions.indices) {
                val action = actions[index]
                val result = action.run(context).get()
                if (!result) {
                    future.complete(false)
                    break
                } else if (index == actions.lastIndex) {
                    future.complete(true)
                }
            }
        }
        return future
    }

}