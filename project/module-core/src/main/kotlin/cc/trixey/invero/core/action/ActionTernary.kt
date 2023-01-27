package cc.trixey.invero.core.action

import cc.trixey.invero.core.serialize.ListScriptKetherSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Invero
 * cc.trixey.invero.core.action.ActionTernary
 *
 * @author Arasple
 * @since 2023/1/26 22:36
 */
interface ActionTernary {

    @Serializable(with = ListScriptKetherSerializer::class)
    val conditions: List<ScriptKether>

    @SerialName("then")
    val accept: Action?

    @SerialName("else")
    val deny: Action?

}