package cc.trixey.invero.bukkit.event

import cc.trixey.invero.bukkit.BukkitViewer
import cc.trixey.invero.bukkit.BukkitWindowHolder
import cc.trixey.invero.common.Viewer
import cc.trixey.invero.common.Window
import cc.trixey.invero.common.event.EventType
import cc.trixey.invero.common.event.WindowItemsCollectEvent
import org.bukkit.event.inventory.InventoryClickEvent

/**
 * Invero
 * cc.trixey.invero.bukkit.event.DelegatedItemsCollectEvent
 *
 * @author Arasple
 * @since 2023/1/6 12:16
 */
@JvmInline
value class DelegatedItemsCollectEvent(
    override val event: InventoryClickEvent
) : WindowItemsCollectEvent, DelegatedInventoryEvent {

    override val viewer: Viewer
        get() = BukkitViewer(event.whoClicked.uniqueId)

    override val window: Window
        get() = (event.clickedInventory!!.holder as BukkitWindowHolder).window

    override val type: EventType
        get() = EventType.ITEMS_COLLECT

    override var isCancelled: Boolean
        get() = event.isCancelled
        set(value) {
            event.isCancelled = value
        }

}