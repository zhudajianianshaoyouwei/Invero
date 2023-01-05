package cc.trixey.invero.common.event

import cc.trixey.invero.bukkit.BukkitViewer
import cc.trixey.invero.common.Window
import cc.trixey.invero.common.event.EventType.*
import org.bukkit.event.inventory.*

/**
 * @author Arasple
 * @since 2023/1/5 14:00
 */
class BukkitWindowEvent(
    val event: InventoryEvent,
    override val viewer: BukkitViewer,
    override val window: Window,
) : WindowEvent {

    override val type: EventType = when (event) {
        is InventoryOpenEvent -> INVENTORY_OPEN
        is InventoryCloseEvent -> INVENTORY_CLOSE
        is InventoryDragEvent -> ITEMS_DRAG
        is InventoryClickEvent -> {
            when (event.action) {
                InventoryAction.MOVE_TO_OTHER_INVENTORY -> ITEMS_MOVE
                InventoryAction.COLLECT_TO_CURSOR -> ITEMS_COLLECT
                else -> CLICK
            }
        }

        else -> error("Unregonized event")
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : InventoryEvent> cast(): T {
        return event as T
    }

}