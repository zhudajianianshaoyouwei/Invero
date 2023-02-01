@file:OptIn(ExperimentalSerializationApi::class)

package cc.trixey.invero.core

import cc.trixey.invero.common.Menu
import cc.trixey.invero.core.*
import cc.trixey.invero.core.menu.MenuBindings
import cc.trixey.invero.core.menu.MenuEvents
import cc.trixey.invero.core.menu.MenuSettings
import cc.trixey.invero.core.serialize.ListAgentPanelSerializer
import cc.trixey.invero.core.util.session
import cc.trixey.invero.core.util.unregisterSession
import cc.trixey.invero.ui.bukkit.PlayerViewer
import cc.trixey.invero.ui.bukkit.api.dsl.chestWindow
import cc.trixey.invero.ui.bukkit.api.dsl.viewer
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames
import org.bukkit.entity.Player
import taboolib.common.platform.function.submitAsync

/**
 * Invero
 * cc.trixey.invero.core.BaseMenu
 *
 * @author Arasple
 * @since 2023/1/15 17:16
 */
@Serializable
class BaseMenu(
    override var id: String?,
    @SerialName("menu")
    val settings: MenuSettings,
    @JsonNames("binding", "activator", "activators")
    val bindings: MenuBindings? = null,
    @JsonNames("event", "listener")
    val events: MenuEvents? = null,
    @Serializable(with = ListAgentPanelSerializer::class)
    @JsonNames("panel", "pane", "panes")
    val panels: List<AgentPanel>,
) : Menu {

    init {
        // auto-rows
        if (settings.rows == null)
            settings.rows = panels.maxBy { it.scale.height }.scale.height.coerceIn(1..6)
    }

    override fun open(viewer: PlayerViewer, vars: Map<String, Any>) {
        // Events_PreOpen
        if (events?.preOpen?.run(Context(viewer))?.get() == false) {
            return
        }
        // 注销原有菜单会话
        if (viewer.session != null) {
            val session = viewer.session
            if (session != null && session.elapsed() < 2_00) {
                return
            } else {
                viewer.unregisterSession()
            }
        }
        // 新建 Window
        val window = chestWindow(
            viewer,
            settings.containerType.rows,
            "",
            settings.storageMode,
            isVirtual(),
        ).onClose { close(viewer, closeWindow = false, closeInventory = false) }
        // 注册会话
        val session = Session.register(viewer, this, window, vars)
        // 开启 Window
        // 其本身会检查是否已经打开任何 Window，并自动关闭等效旧菜单的 Window
        window.preOpen { panels.forEach { it.invoke(window, session) } }
        window.preRender { updateTitle(session) }
        window.open()
        settings.title.invoke(session)
        // Events_PostOpen
        events?.postOpen?.run(Context(viewer, session))
    }

    override fun close(viewer: PlayerViewer, closeWindow: Boolean, closeInventory: Boolean) {
        val session = viewer.session ?: return
        if (session.menu != this) return
        viewer.unregisterSession { if (closeWindow) it.close(true, closeInventory) }

        // Events_Close
        if (events?.close?.run(Context(viewer, session))?.get() == false) {
            // Unclosable menu
            // Not recommended
            submitAsync { open(viewer) }
        }
    }

    override fun register() {
        bindings?.register(this)
    }

    override fun unregister() {
        bindings?.unregister()
    }

    fun updateTitle(session: Session) {
        session.window.title = session.parse(settings.title.default)
    }

    override fun isVirtual(): Boolean {
        return settings.virtual && !panels.any { it.requireBukkitWindow() }
    }

}