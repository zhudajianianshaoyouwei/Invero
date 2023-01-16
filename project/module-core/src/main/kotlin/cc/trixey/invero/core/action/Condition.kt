package cc.trixey.invero.core.action

import cc.trixey.invero.core.session.Session
import taboolib.common5.cbool
import java.util.concurrent.CompletableFuture

/**
 * Invero
 * cc.trixey.invero.core.action.Condition
 *
 * @author Arasple
 * @since 2023/1/14 14:16
 */
interface Condition : Expression {

    fun eval(session: Session): CompletableFuture<Boolean> {
        return invoke(session).thenApply { it.cbool }
    }

}