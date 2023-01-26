@file:OptIn(ExperimentalSerializationApi::class)

package cc.trixey.invero.core.menu

import cc.trixey.invero.bukkit.PlayerViewer
import cc.trixey.invero.bukkit.api.dsl.chestWindow
import cc.trixey.invero.core.*
import cc.trixey.invero.core.serialize.ListAgentPanelSerializer
import cc.trixey.invero.core.util.session
import cc.trixey.invero.core.util.unregisterSession
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames
import taboolib.common.platform.function.submitAsync

/**
 * Invero
 * cc.trixey.invero.core.menu.StandardMenu
 *
 * @author Arasple
 * @since 2023/1/15 17:16
 */
@Serializable
class StandardMenu(
    @SerialName("menu")
    val settings: MenuSettings,
    @JsonNames("binding", "activator", "activators")
    val bindings: MenuBindings? = null,
    @JsonNames("event", "listener")
    val events: MenuEvents? = null,
    @Serializable(with = ListAgentPanelSerializer::class)
    @JsonNames("panel", "pane", "panes")
    override val panels: List<AgentPanel>
) : Menu() {

    override fun open(viewer: PlayerViewer, variables: Map<String, Any>): Session? {
        // Events_PreOpen
        if (events?.preOpen?.run(Context(viewer))?.get() == false) {
            return null
        }
        // 注销原有菜单会话
        if (viewer.session != null) {
            val session = viewer.session
            if (session != null && session.elapsed() < 2_00) {
                return null
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
        val session = Session.register(viewer, this, window, variables)
        // 开启 Window
        // 其本身会检查是否已经打开任何 Window，并自动关闭等效旧菜单的 Window
        window.preOpen { panels.forEach { it.invoke(window, session) } }
        window.preRender { updateTitle(session) }
        updateTitle(session)
        window.open()
        settings.title.invoke(session)
        // Events_PostOpen
        events?.postOpen?.run(Context(viewer, session))

        return session
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

    override fun unregister() {
        bindings?.unregister()
    }

    override fun updateTitle(session: Session) {
        session.window.title = session.parse(settings.title.default)
    }

    override fun isVirtual(): Boolean {
        return settings.virtual && !panels.any { it.requireBukkitWindow() }
    }

}