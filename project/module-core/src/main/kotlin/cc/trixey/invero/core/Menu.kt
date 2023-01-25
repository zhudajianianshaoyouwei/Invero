package cc.trixey.invero.core

import cc.trixey.invero.bukkit.PlayerViewer
import cc.trixey.invero.core.serialize.SelectorMenu
import kotlinx.serialization.Serializable
import org.bukkit.entity.Player

/**
 * Invero
 * cc.trixey.invero.core.AbstractMenu
 *
 * @author Arasple
 * @since 2023/1/24 21:27
 */
@Serializable(with = SelectorMenu::class)
abstract class Menu {

    var name: String? = null

    abstract val panels: List<AgentPanel>

    fun open(player: Player, variables: Map<String, Any> = emptyMap()) = open(PlayerViewer(player), variables)

    fun close(player: Player) = close(PlayerViewer(player))

    abstract fun open(viewer: PlayerViewer, variables: Map<String, Any> = emptyMap()): Session?

    abstract fun close(viewer: PlayerViewer, closeWindow: Boolean = true, closeInventory: Boolean = true)

    abstract fun updateTitle(session: Session)

    abstract fun isVirtual(): Boolean

    abstract fun unregister()

}