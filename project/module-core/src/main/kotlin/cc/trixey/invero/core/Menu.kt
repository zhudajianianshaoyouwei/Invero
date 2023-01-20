@file:OptIn(ExperimentalSerializationApi::class)

package cc.trixey.invero.core

import cc.trixey.invero.bukkit.PlayerViewer
import cc.trixey.invero.bukkit.api.dsl.chestWindow
import cc.trixey.invero.core.serialize.ListAgentPanelSerializer
import cc.trixey.invero.core.util.session
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
        val session = viewer.session

        submit {
            val window = chestWindow(
                viewer,
                settings.containerType.rows,
                settings.title.getDefault(),
                settings.options.storageMode,
                isVirtualMenu(),
            ).onClose {
                session.paired = null
                session.unregisterAll()
            }

            // 开启 Window
            // 其本身会检查是否已经打开任何 Window，并自动关闭等效旧菜单的 Window
            window.open()
            // 设置新的 Menu，Window
            session.paired = this@Menu to window

            // 部署
            panels.forEach { it.invoke(window, session) }
            settings.title.invoke(session)
        }
    }

    fun close(viewer: PlayerViewer, closeWindow: Boolean = true, closeInventory: Boolean = true) {
        val session = viewer.session
        val window = session.window ?: error("Not found window when closing the menu [$name]")

        session.paired = null
        session.unregisterAll()

        if (closeWindow) window.close(closeInventory)
    }

    private fun isVirtualMenu(): Boolean {
        return !panels.any { it.requireBukkitWindow() } && settings.packet
    }

}