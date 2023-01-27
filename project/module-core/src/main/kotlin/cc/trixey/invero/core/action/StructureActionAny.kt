package cc.trixey.invero.core.action

import cc.trixey.invero.core.Context
import cc.trixey.invero.core.serialize.ListScriptKetherSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import taboolib.common5.cbool
import java.util.concurrent.CompletableFuture

/**
 * Invero
 * cc.trixey.invero.core.action.StructureActionAny
 *
 * @author Arasple
 * @since 2023/1/26 22:42
 */
@Serializable
class StructureActionAny(
    @SerialName("any")
    override val conditions: List<ScriptKether>,
    override val accept: Action?,
    override val deny: Action?
) : ActionTernary, Action() {

    init {
        require(accept != null || deny != null) { "At least one type of response is required for IF structure" }
    }

    override fun run(context: Context): CompletableFuture<Boolean> {
        var future = CompletableFuture.completedFuture(true)
        conditions.forEach { cond ->
            future = future.thenCombine(cond.eval(context)) { b, o -> b || o.cbool }
        }
        return future.thenCompose {
            (if (it) accept?.run(context) else deny?.run(context)) ?: CompletableFuture.completedFuture(true)
        }
    }

}