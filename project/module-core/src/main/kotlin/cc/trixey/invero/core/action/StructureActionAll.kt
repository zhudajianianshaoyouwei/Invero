package cc.trixey.invero.core.action

import cc.trixey.invero.core.Context
import cc.trixey.invero.core.serialize.ListScriptKetherSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import taboolib.common5.cbool
import java.util.concurrent.CompletableFuture

/**
 * Invero
 * cc.trixey.invero.core.action.StructureActionAll
 *
 * @author Arasple
 * @since 2023/1/26 22:42
 */
@Serializable
class StructureActionAll(
    @SerialName("all")
    @Serializable(with = ListScriptKetherSerializer::class)
    override val conditions: List<ScriptKether>,
    override val then: Action?,
    override val `else`: Action?
) : ActionTernary, Action() {

    init {
        require(then != null || `else` != null) { "At least one type of response is required for IF structure" }
    }

    override fun run(context: Context): CompletableFuture<Boolean> {
        var future = CompletableFuture.completedFuture(true)
        conditions.forEach { cond ->
            future = future.thenCombine(cond.eval(context)) { b, o -> b && o.cbool }
        }
        return future.thenCompose {
            (if (it) then?.run(context) else `else`?.run(context)) ?: CompletableFuture.completedFuture(true)
        }
    }

}