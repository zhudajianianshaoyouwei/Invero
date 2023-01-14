package cc.trixey.invero.core.impl.action

import cc.trixey.invero.core.action.Comparator
import cc.trixey.invero.core.action.Expression
import cc.trixey.invero.core.menu.MenuContext
import java.util.concurrent.CompletableFuture

/**
 * Invero
 * cc.trixey.invero.core.impl.action.ActionWhen
 *
 * @author Arasple
 * @since 2023/1/14 12:43
 */
class ActionWhen(val value: Expression, val response: Map<Comparator, MAction>) : MAction() {

    override fun run(context: MenuContext): CompletableFuture<Boolean> {
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