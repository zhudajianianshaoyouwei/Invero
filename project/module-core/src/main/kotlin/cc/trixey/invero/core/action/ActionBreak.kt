package cc.trixey.invero.core.action

import cc.trixey.invero.core.session.Session
import java.util.concurrent.CompletableFuture

/**
 * Invero
 * cc.trixey.invero.core.action.ActionBreak
 *
 * @author Arasple
 * @since 2023/1/15 22:40
 */
class ActionBreak : Action {

    override fun run(session: Session): CompletableFuture<Boolean> {
        return COMPLETABLE_FALSE
    }

    companion object {

        val COMPLETABLE_FALSE = CompletableFuture<Boolean>().also { it.complete(false) }

    }

}