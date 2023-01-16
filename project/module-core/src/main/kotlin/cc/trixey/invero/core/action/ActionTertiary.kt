package cc.trixey.invero.core.action

import cc.trixey.invero.Session
import java.util.concurrent.CompletableFuture

/**
 * Invero
 * cc.trixey.invero.core.action.ActionTertiary
 *
 * @author Arasple
 * @since 2023/1/15 22:42
 */
class ActionTertiary(
    val condition: Condition,
    val accept: Action,
    val deny: Action
) : Action {

    override fun run(session: Session): CompletableFuture<Boolean> {
        return condition.eval(session).thenCompose {
            if (it) accept.run(session)
            else deny.run(session)
        }
    }

}