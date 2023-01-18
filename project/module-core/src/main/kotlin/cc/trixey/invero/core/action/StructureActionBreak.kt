package cc.trixey.invero.core.action

import cc.trixey.invero.core.Context
import java.util.concurrent.CompletableFuture

/**
 * Invero
 * cc.trixey.invero.core.action.StructureActionBreak
 *
 * @author Arasple
 * @since 2023/1/15 22:40
 */
class StructureActionBreak : Action() {

    override fun run(context: Context): CompletableFuture<Boolean> {
        return COMPLETABLE_FALSE
    }

    companion object {

        val COMPLETABLE_FALSE = CompletableFuture<Boolean>().also { it.complete(false) }

    }

}