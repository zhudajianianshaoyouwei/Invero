package cc.trixey.invero.impl

import cc.trixey.invero.bukkit.api.dsl.bukkitChestWindow
import cc.trixey.invero.bukkit.api.dsl.packetChestWindow
import cc.trixey.invero.common.ContainerType
import cc.trixey.invero.common.StorageMode
import cc.trixey.invero.common.Viewer
import cc.trixey.invero.core.animation.CycleMode
import cc.trixey.invero.core.menu.Menu
import cc.trixey.invero.core.menu.MenuOptions
import cc.trixey.invero.core.menu.PanelAgent

/**
 * Invero
 * cc.trixey.invero.impl.DefaultMenu
 *
 * @author Arasple
 * @since 2023/1/14 16:51
 */
class DefaultMenu(
    val name: String,
    override val title: Array<String>,
    override val titleFramePeriod: Int,
    override val titleFrameMode: CycleMode,
    override val type: ContainerType,
    override val storageMode: StorageMode,
    override val options: DefaultMenuOptions,
    val panelAgents: List<PanelAgent<*>>
) : Menu {

    override fun open(viewer: Viewer) {
        val session = viewer.getMenuSession()
        val window = if (isIOWindow()) {
            bukkitChestWindow(type.rows, title[0], storageMode)
        } else {
            packetChestWindow(type.rows, title[0], storageMode)
        }

        panelAgents.forEach { it.apply(session) }

        session.menu = this
        session.viewingWindow = window

        window.open(viewer)
    }

    override fun close(viewer: Viewer) {
        val session = viewer.getMenuSession()

        session.taskClosure()
        session.menu = this
        session.viewingWindow?.close(viewer)
        session.viewingWindow = null
    }

    private fun isIOWindow(): Boolean {
        return false
    }

}