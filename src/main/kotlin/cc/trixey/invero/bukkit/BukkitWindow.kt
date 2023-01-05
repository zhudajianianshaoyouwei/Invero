package cc.trixey.invero.bukkit

import cc.trixey.invero.common.WindowType
import cc.trixey.invero.common.event.BukkitWindowEvent
import cc.trixey.invero.common.event.EventType
import cc.trixey.invero.common.event.WindowEvent
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.event.inventory.InventoryOpenEvent

/**
 * @author Arasple
 * @since 2022/12/29 12:54
 */
abstract class BukkitWindow(type: WindowType, title: String = "Untitled_Invero_Window") : BaseWindow(type, title) {

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