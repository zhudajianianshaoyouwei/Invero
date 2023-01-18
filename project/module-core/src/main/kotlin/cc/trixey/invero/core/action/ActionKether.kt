package cc.trixey.invero.core.action

import cc.trixey.invero.core.Context
import cc.trixey.invero.core.serialize.ActionKetherSerializer
import kotlinx.serialization.Serializable
import java.util.concurrent.CompletableFuture

/**
 * Invero_Core
 * cc.trixey.invero.core.action.ActionKether
 *
 * @author Arasple
 * @since 2023/1/18 11:30
 */
@Serializable(with = ActionKetherSerializer::class)
class ActionKether(val script: String) : Action() {

    override fun run(context: Context): CompletableFuture<Boolean> {
        return StructureActionBreak.COMPLETABLE_FALSE
    }

}