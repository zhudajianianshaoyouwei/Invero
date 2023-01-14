package cc.trixey.invero.core.action

import cc.trixey.invero.core.Context
import java.util.concurrent.CompletableFuture

/**
 * Invero
 * cc.trixey.invero.core.action.ActionWhen
 *
 * @author Arasple
 * @since 2023/1/14 12:43
 */
class ActionWhen(val value: Expression, val response: Map<Comparator, Action>) : Action {

    override fun run(context: Context): CompletableFuture<Boolean> {
        return value.invoke(context).thenCompose { switchBy ->
            response.forEach {
                if (it.key.matches(switchBy)) {
                    return@thenCompose it.value.run(context)
                }
            }
            return@thenCompose ActionBreak.COMPLETABLE_FALSE
        }
    }

}