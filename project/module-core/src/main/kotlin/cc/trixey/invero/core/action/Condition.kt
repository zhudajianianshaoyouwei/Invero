package cc.trixey.invero.core.action

import cc.trixey.invero.core.Context
import cc.trixey.invero.core.util.safeBoolean
import java.util.concurrent.CompletableFuture

/**
 * Invero
 * cc.trixey.invero.core.action.Condition
 *
 * @author Arasple
 * @since 2023/1/14 14:16
 */
interface Condition : Expression {

    fun eval(context: Context): CompletableFuture<Boolean> {
        return invoke(context).thenApply { it.safeBoolean() }
    }

}