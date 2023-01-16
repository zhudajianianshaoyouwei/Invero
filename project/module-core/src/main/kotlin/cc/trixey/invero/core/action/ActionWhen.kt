package cc.trixey.invero.core.action

import cc.trixey.invero.Session
import java.util.concurrent.CompletableFuture

/**
 * Invero
 * cc.trixey.invero.core.action.ActionWhen
 *
 * @author Arasple
 * @since 2023/1/15 22:42
 */
class ActionWhen(val value: Expression, val response: Map<Comparator, Action>) : Action {

    override fun run(session: Session): CompletableFuture<Boolean> {
        return value.invoke(session).thenCompose { switchBy ->
            response.forEach {
                if (it.key.matches(switchBy)) {
                    return@thenCompose it.value.run(session)
                }
            }
            return@thenCompose ActionBreak.COMPLETABLE_FALSE
        }
    }

}