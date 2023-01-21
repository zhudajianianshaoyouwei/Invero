@file:OptIn(ExperimentalSerializationApi::class)

package cc.trixey.invero.core

import cc.trixey.invero.bukkit.PlayerViewer
import cc.trixey.invero.bukkit.api.dsl.chestWindow
import cc.trixey.invero.core.serialize.ListAgentPanelSerializer
import cc.trixey.invero.core.util.session
import cc.trixey.invero.core.util.unregisterSession
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.json.JsonNames
import org.bukkit.entity.Player

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
        // 注销原有菜单会话
        if (viewer.session != null) {
            val session = viewer.unregisterSession()
            if (session != null && session.elapsed() < 2_00) return
        }
        // 新建 Window
        val window = chestWindow(
            viewer,
            settings.containerType.rows,
            settings.title.getDefault(),
            settings.options.storageMode,
            isVirtualMenu(),
        ).onClose { close(viewer, closeWindow = false, closeInventory = false) }
        // 注册会话
        val session = Session.register(viewer, this, window)
        // 开启 Window
        // 其本身会检查是否已经打开任何 Window，并自动关闭等效旧菜单的 Window
        window.postRender { panels.forEach { it.invoke(window, session) } }
        window.open()
        settings.title.invoke(session)
    }

    fun close(viewer: PlayerViewer, closeWindow: Boolean = true, closeInventory: Boolean = true) {
        val session = viewer.session ?: error("Not found registered session")
        require(session.menu == this) { "Error menu handler" }

        viewer.unregisterSession { if (closeWindow) it.close(true, closeInventory) }
    }

    private fun isVirtualMenu(): Boolean {
        return !panels.any { it.requireBukkitWindow() } && settings.packet
    }

}