@file:OptIn(ExperimentalSerializationApi::class)

package cc.trixey.invero.core

import cc.trixey.invero.bukkit.PlayerViewer
import cc.trixey.invero.bukkit.api.dsl.chestWindow
import cc.trixey.invero.core.serialize.ListAgentPanelSerializer
import cc.trixey.invero.core.util.getSession
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.json.JsonNames
import org.bukkit.entity.Player
import taboolib.common.platform.function.submit

/**
 * Invero
 * cc.trixey.invero.core.Menu
 *
 * @author Arasple
 * @since 2023/1/15 17:16
 */
@Serializable
class Menu(
    @Transient
    var name: String? = null,
    @SerialName("menu")
    val settings: MenuSettings,
    @Serializable(with = ListAgentPanelSerializer::class)
    @JsonNames("panel", "pane", "panes")
    val panels: List<AgentPanel>
) {

    fun open(player: Player) {
        return open(PlayerViewer(player))
    }

    fun close(player: Player) {
        return close(PlayerViewer(player))
    }

    fun open(viewer: PlayerViewer) {
        val session = viewer.getSession()

        if (session.menu != null) {
            session.unregisterTasks(session.window!!)
        }

        submit {
            val window = chestWindow(
                viewer,
                settings.containerType.rows,
                settings.title.getDefault(),
                settings.options.storageMode,
                isVirtualMenu(),
            ).onClose { session.unregisterTasks(it) }

            session.menu = this@Menu
            session.window = window
            panels.forEach { it.invoke(window, session) }
            window.open()
            settings.title.invoke(session)
        }
    }

    fun close(viewer: PlayerViewer, closeInventory: Boolean = true) {
        val session = viewer.getSession()
        val window = session.window ?: error("No available window")

        session.unregisterTasks(window)
        session.menu = null
        session.window = null

        window.close(closeInventory)
    }

    private fun isVirtualMenu(): Boolean {
        return !panels.any { it.requireBukkitWindow() } && settings.packet
    }

}