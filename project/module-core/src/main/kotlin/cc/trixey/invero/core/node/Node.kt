package cc.trixey.invero.core.node

import cc.trixey.invero.core.Context
import cc.trixey.invero.core.Session
import cc.trixey.invero.core.node.Node.Type.JAVASCRIPT
import cc.trixey.invero.core.node.Node.Type.KETHER
import cc.trixey.invero.core.serialize.NodeTypeSerializer
import cc.trixey.invero.core.util.KetherHandler
import cc.trixey.invero.core.util.runJS
import kotlinx.serialization.Serializable
import org.bukkit.entity.Player

/**
 * Invero
 * cc.trixey.invero.core.node.Node
 *
 * @author Arasple
 * @since 2023/2/8 11:34
 */
@Serializable
class Node(val type: Type, val value: String) {

    fun invoke(session: Session, variables: Map<String, Any>): String {
        val context = session.getVariable("@context") as? Context
        val viewer = session.viewer.get<Player>()

        return when (type) {
            KETHER -> KetherHandler.invoke(value, viewer, session.getVariables(context?.variables) + variables)
                .getNow("<TIMEOUT: KETHER>")

            JAVASCRIPT -> runJS(value, session, variables)
                .getNow("<TIMEOUT: JAVASCRIPT>")

            else -> error("Can not invoke none-script type node")
        }.toString()
    }

    @Serializable(with = NodeTypeSerializer::class)
    enum class Type {

        CONST,

        TEXT,

        KETHER,

        JAVASCRIPT

    }

}