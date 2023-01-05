package cc.trixey.invero.bukkit

import cc.trixey.invero.common.Viewer
import cc.trixey.invero.common.WindowType
import cc.trixey.invero.common.event.BukkitWindowEvent
import cc.trixey.invero.common.event.EventType
import cc.trixey.invero.common.event.WindowEvent
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.event.inventory.InventoryOpenEvent

/**
 * @author Arasple
 * @since 2022/12/29 12:54
 */
abstract class BukkitWindow(type: WindowType, title: String) : BaseWindow(type, title) {

    abstract override val inventory: BukkitInventory

    fun open(player: Player) = open(BukkitViewer(player))

    override fun render() {
        panels.sortedBy { it.weight }.forEach {
            it.render()
        }
    }

    override fun open(viewer: Viewer) {
        if (viewers.add(viewer)) {
            inventory.open(viewer)
            render()
        } else {
            error("Viewer {$viewer} is already viewing this window")
        }
    }

    override fun close(viewer: Viewer) {
        if (viewers.remove(viewer)) inventory.close(viewer)
    }

    open fun handleClick(e: InventoryClickEvent) {
        val click = scale.toPos(e.rawSlot)

        panels
            .sortedByDescending { it.weight }
            .forEach {
                if (click in it.area) {
                    it as BukkitPanel

                    it.handleClick(click - it.locate, e)
                    return@forEach
                }
            }
    }

    open fun handleDrag(e: InventoryDragEvent) {}

    open fun handleItemsMove(e: InventoryClickEvent) {}

    open fun handleItemsCollect(e: InventoryClickEvent) {}

    open fun handleOpen(e: InventoryOpenEvent) {}

    open fun handleClose(e: InventoryCloseEvent) {}

    override fun handleEvent(e: WindowEvent) {
        e as BukkitWindowEvent

        when (e.type) {
            EventType.INVENTORY_OPEN -> handleOpen(e.cast())
            EventType.INVENTORY_CLOSE -> handleClose(e.cast())
            EventType.ITEMS_DRAG -> handleDrag(e.cast())
            EventType.ITEMS_MOVE -> handleItemsMove(e.cast())
            EventType.ITEMS_COLLECT -> handleItemsCollect(e.cast())
            EventType.CLICK -> handleClick(e.cast())
        }
    }


}