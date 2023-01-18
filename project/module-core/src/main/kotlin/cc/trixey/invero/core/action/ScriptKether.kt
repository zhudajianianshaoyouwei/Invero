package cc.trixey.invero.core.action

import cc.trixey.invero.core.Context
import cc.trixey.invero.core.util.KetherHandler
import cc.trixey.invero.core.serialize.ScriptKetherSerializer
import kotlinx.serialization.Serializable
import java.util.concurrent.CompletableFuture

/**
 * Invero
 * cc.trixey.invero.core.action.ScriptKether
 *
 * @author Arasple
 * @since 2023/1/16 13:11
 */
@JvmInline
@Serializable(with = ScriptKetherSerializer::class)
value class ScriptKether(val script: String) : Condition {

    override fun invoke(context: Context): CompletableFuture<Any?> {
        val player = context.player

        return KetherHandler.invoke(script, player, context.variables)
    }

}