package cc.trixey.invero.bukkit

import cc.trixey.invero.bukkit.api.InveroAPI
import cc.trixey.invero.bukkit.nms.updateTitle
import cc.trixey.invero.common.Panel
import cc.trixey.invero.common.Viewer
import cc.trixey.invero.common.Window
import cc.trixey.invero.common.WindowType
import cc.trixey.invero.common.event.*
import org.bukkit.entity.Player
import taboolib.common.platform.function.submit

/**
 * @author Arasple
 * @since 2022/12/29 12:54
 */
abstract class BukkitWindow(val type: WindowType, title: String = "Untitled_Invero_Window") : Window {

    abstract override val inventory: BukkitInventory

    override var title = title
        set(value) {
            field = value
            submit(async = true) {
                updateTitle(value, true)
            }
        }

    override val viewers = mutableSetOf<Viewer>()

    override val panels = arrayListOf<Panel>()

    override val size = type.entireWindowSize

    fun open(player: Player) = open(BukkitViewer(player))

    override fun render() {
        panels.sortedBy { it.weight }.forEach {
            it.render()
        }
    }

    override fun open(viewer: Viewer) {
        if (viewers.add(viewer)) {
            if (viewers.size == 1) InveroAPI.manager.register(this)
            inventory.open(viewer)
            render()
        } else {
            error("Viewer {$viewer} is already viewing this window")
        }
    }

    override fun close(viewer: Viewer) {
        if (viewers.remove(viewer)) inventory.close(viewer)
        if (viewers.isEmpty()) {
            InveroAPI.manager.unregister(this)
        }
    }

    override fun handleDrag(e: WindowDragEvent) {

    }

    override fun handleItemsCollect(e: WindowItemsCollectEvent) {

    }

    override fun handleItemsMove(e: WindowItemsMoveEvent) {

    }

    override fun handleOpen(e: WindowOpenEvent) {

    }

    override fun handleClose(e: WindowCloseEvent) {
        close(e.getViewer())
    }

    override fun handleClick(e: WindowClickEvent) {
        val clickedSlot = scale.convertToPosition(e.rawSlot)

        panels
            .sortedByDescending { it.weight }
            .forEach {
                if (clickedSlot in it.area) {
                    it.handleClick(clickedSlot - it.locate, e)
                    it.passClickEventHandler(e)
                    return@forEach
                }
            }
    }

}