@file:OptIn(ExperimentalSerializationApi::class)

package cc.trixey.invero.core

import cc.trixey.invero.common.Menu
import cc.trixey.invero.common.util.prettyPrint
import cc.trixey.invero.core.*
import cc.trixey.invero.core.menu.*
import cc.trixey.invero.core.panel.PanelCrafting
import cc.trixey.invero.core.serialize.ListAgentPanelSerializer
import cc.trixey.invero.core.serialize.NodeSerializer
import cc.trixey.invero.core.util.session
import cc.trixey.invero.core.util.unregisterSession
import cc.trixey.invero.ui.bukkit.InventoryPacket
import cc.trixey.invero.ui.bukkit.InventoryVanilla
import cc.trixey.invero.ui.bukkit.PlayerViewer
import cc.trixey.invero.ui.bukkit.api.dsl.commonWindow
import cc.trixey.invero.ui.bukkit.panel.CraftingPanel
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames
import org.bukkit.entity.Player
import taboolib.common.platform.function.submitAsync
import taboolib.library.reflex.Reflex.Companion.setProperty
import taboolib.platform.util.giveItem

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
    val bindings: MenuBindings?,
    @JsonNames("event", "listener")
    val events: MenuEvents?,
    @JsonNames("node", "scripts")
    val nodes: Map<String, @Serializable(with = NodeSerializer::class) NodeRunnable>?,
    @JsonNames("task")
    val tasks: Map<String, MenuTask>?,
    @Serializable(with = ListAgentPanelSerializer::class)
    @JsonNames("panel", "pane", "panes")
    val panels: List<AgentPanel>,
) : Menu {

    init {
        // auto-rows
        if (settings.rows == null)
            settings.rows = panels
                .maxBy { it.scale?.height ?: 0 }
                .scale
                ?.height?.coerceIn(1..6) ?: 3
        // auto-override
        if (panels.any { it is PanelCrafting }) {
            settings.setProperty("overridePlayerInventory", false)
        }
    }

    override fun open(viewer: PlayerViewer, vars: Map<String, Any>) {
        // 预开启动作
        if (events?.preOpen?.run(Context(viewer))?.get() == false) {
            return
        }
        // 注销原有菜单会话
        if (viewer.session != null) {
            val session = viewer.session
            if (session != null && session.elapsed() < 2_00) return
            else viewer.unregisterSession()
        }
        // 新建 Window
        val window = commonWindow(
            viewer,
            virtual = isVirtual(),
            type = settings.containerType,
            storageMode = settings.storageMode
        ).onClose {
            // restore collection
            val player = viewer.get<Player>()
            it.panels.filterIsInstance<CraftingPanel>().forEach { panel ->
                player.giveItem(panel.freeSlots.mapNotNull { slot -> panel.storage[slot] })
            }
            // close callback (internal)
            close(viewer, closeWindow = false, closeInventory = false)
        }
        // 注册会话
        val session = Session.register(viewer, this, window, vars)
        try {
            // 开启 Window
            // 其本身会检查是否已经打开任何 Window，并自动关闭等效旧菜单的 Window
            window.preOpen { panels.forEach { it.invoke(window, session) } }
            window.onOpen { updateTitle(session) }
            window.open()
            // 频繁交互屏蔽
            if (isVirtual())
                (window.inventory as InventoryPacket).onClick { _, _ -> viewer.canInteract }
            else
                (window.inventory as InventoryVanilla).onClick { _ -> viewer.canInteract }
            // 应用动态标题属性
            settings.title.submit(session)
            // 应用周期事件
            tasks?.forEach { it.value.submit(session) }
            // 开启后事件动作
            events?.postOpen?.run(Context(viewer, session))
        } catch (e: Throwable) {
            e.prettyPrint()
            Session.unregister(session)
        }
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

    val PlayerViewer.canInteract: Boolean
        get() = settings.interactBaffle.hasNext(name)


}