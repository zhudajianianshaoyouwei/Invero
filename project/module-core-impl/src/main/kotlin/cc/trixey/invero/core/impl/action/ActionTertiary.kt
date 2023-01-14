package cc.trixey.invero.core.impl.action

import cc.trixey.invero.core.action.Condition
import cc.trixey.invero.core.menu.MenuContext
import java.util.concurrent.CompletableFuture

/**
 * Invero
 * cc.trixey.invero.core.impl.action.ActionTertiary
 *
 * @author Arasple
 * @since 2023/1/14 12:20
 */
class ActionTertiary(val condition: Condition, val accept: MAction, val deny: MAction) : MAction() {

    override fun run(context: MenuContext): CompletableFuture<Boolean> {
        return condition.eval(context).thenCompose {
            if (it) accept.run(context)
            else deny.run(context)
        }
    }

}