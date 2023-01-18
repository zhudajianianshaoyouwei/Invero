package cc.trixey.invero.core.action

import cc.trixey.invero.core.Context
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.concurrent.CompletableFuture

/**
 * Invero
 * cc.trixey.invero.core.action.StructureActionWhen
 *
 * @author Arasple
 * @since 2023/1/15 22:42
 */
@Serializable
class StructureActionWhen(
    val case: ScriptKether,
    @SerialName("when")
    val response: Map<Comparator, Action>
) : Action() {

    override fun run(context: Context): CompletableFuture<Boolean> = case.invoke(context).thenCompose { switchBy ->
        val case = switchBy.toString()

        response.forEach {
            if (it.key.compare(case)) {
                return@thenCompose it.value.run(context)
            }
        }

        return@thenCompose StructureActionBreak.COMPLETABLE_FALSE
    }

}