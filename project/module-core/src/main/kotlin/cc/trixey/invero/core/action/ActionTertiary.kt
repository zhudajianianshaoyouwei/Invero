package cc.trixey.invero.core.action

import cc.trixey.invero.core.Context
import java.util.concurrent.CompletableFuture

/**
 * Invero
 * cc.trixey.invero.core.action.ActionTertiary
 *
 * @author Arasple
 * @since 2023/1/14 12:20
 */
class ActionTertiary(val condition: Condition, val accept: Action, val deny: Action) : Action {

    override fun run(context: Context): CompletableFuture<Boolean> {
        return condition.eval(context).thenCompose {
            if (it) accept.run(context)
            else deny.run(context)
        }
    }

}