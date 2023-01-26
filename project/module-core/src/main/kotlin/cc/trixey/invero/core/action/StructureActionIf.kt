package cc.trixey.invero.core.action

import cc.trixey.invero.core.Context
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.concurrent.CompletableFuture

/**
 * Invero
 * cc.trixey.invero.core.action.StructureActionIf
 *
 * @author Arasple
 * @since 2023/1/15 22:42
 */
@Serializable
class StructureActionIf(
    @SerialName("if")
    val condition: ScriptKether,
    @SerialName("then")
    val accept: Action?,
    @SerialName("else")
    val deny: Action?
) : Action() {

    init {
        require(accept != null || deny != null) { "At least one type of response is required for IF structure" }
    }

    override fun run(context: Context): CompletableFuture<Boolean> {
        return condition.eval(context).thenCompose {
            (if (it) accept?.run(context) else deny?.run(context))
                ?: CompletableFuture.completedFuture(true)
        }
    }

}