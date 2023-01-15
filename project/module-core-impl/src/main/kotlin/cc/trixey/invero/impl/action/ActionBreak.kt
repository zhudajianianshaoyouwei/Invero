package cc.trixey.invero.impl.action

import cc.trixey.invero.core.menu.MenuContext
import java.util.concurrent.CompletableFuture

/**
 * Invero
 * cc.trixey.invero.impl.action.ActionBreak
 *
 * @author Arasple
 * @since 2023/1/14 12:54
 */
class ActionBreak : MAction() {

    override fun run(context: MenuContext): CompletableFuture<Boolean> {
        return COMPLETABLE_FALSE
    }

    companion object {

        val COMPLETABLE_FALSE = CompletableFuture<Boolean>().also { it.complete(false) }

    }

}