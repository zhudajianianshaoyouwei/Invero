package cc.trixey.invero.core.action

import cc.trixey.invero.Session
import java.util.concurrent.CompletableFuture

/**
 * Invero
 * cc.trixey.invero.core.ActionStructure
 *
 * @author Arasple
 * @since 2023/1/14 12:18
 */
interface Action {

    fun run(session: Session): CompletableFuture<Boolean>

}