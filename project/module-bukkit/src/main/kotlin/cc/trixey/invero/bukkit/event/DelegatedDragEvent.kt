package cc.trixey.invero.bukkit.event

import cc.trixey.invero.bukkit.BukkitViewer
import cc.trixey.invero.bukkit.BukkitWindowHolder
import cc.trixey.invero.common.Viewer
import cc.trixey.invero.common.Window
import cc.trixey.invero.common.event.EventType
import cc.trixey.invero.common.event.WindowDragEvent
import org.bukkit.event.inventory.InventoryDragEvent

/**
 * Invero
 * cc.trixey.invero.bukkit.event.DelegatedDragEvent
 *
 * @author Arasple
 * @since 2023/1/6 11:53
 */
@JvmInline
value class DelegatedDragEvent(
    override val event: InventoryDragEvent
) : WindowDragEvent, DelegatedInventoryEvent {

    override var isCancelled: Boolean
        get() = event.isCancelled
        set(value) {
            event.isCancelled = value
        }

    override fun getViewer(): Viewer {
        return BukkitViewer(event.whoClicked.uniqueId)
    }

    override fun getWindow(): Window {
        return (event.inventory.holder as BukkitWindowHolder).window
    }

    override fun getType(): EventType {
        return EventType.ITEMS_DRAG
    }

}