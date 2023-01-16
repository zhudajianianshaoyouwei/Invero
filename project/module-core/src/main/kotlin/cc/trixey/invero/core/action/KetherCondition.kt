package cc.trixey.invero.core.action

import cc.trixey.invero.Session
import cc.trixey.invero.core.util.KetherHandler
import cc.trixey.invero.serialize.SerializerKetherCondition
import kotlinx.serialization.Serializable
import java.util.concurrent.CompletableFuture

/**
 * Invero
 * cc.trixey.invero.core.action.KetherCondition
 *
 * @author Arasple
 * @since 2023/1/16 13:11
 */
@JvmInline
@Serializable(with = SerializerKetherCondition::class)
value class KetherCondition(val script: String) : Condition {

    override fun invoke(session: Session): CompletableFuture<Any?> {
        val player = session.viewer.get()

        return KetherHandler.invoke(script, player, session.generateVariables())
    }

}