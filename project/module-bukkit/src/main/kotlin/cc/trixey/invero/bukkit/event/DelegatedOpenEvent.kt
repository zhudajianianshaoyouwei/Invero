package cc.trixey.invero.bukkit.event

import cc.trixey.invero.bukkit.BukkitViewer
import cc.trixey.invero.bukkit.BukkitWindowHolder
import cc.trixey.invero.common.Viewer
import cc.trixey.invero.common.Window
import cc.trixey.invero.common.event.EventType
import cc.trixey.invero.common.event.WindowOpenEvent
import org.bukkit.event.inventory.InventoryOpenEvent

/**
 * @author Arasple
 * @since 2023/1/6 11:53
 */
@JvmInline
value class DelegatedOpenEvent(
    override val event: InventoryOpenEvent
) : WindowOpenEvent, DelegatedInventoryEvent {

    override fun getViewer(): Viewer {
        return BukkitViewer(event.player.uniqueId)
    }

    override fun getWindow(): Window {
        return (event.inventory.holder as BukkitWindowHolder).window
    }

    override fun getType(): EventType {
        return EventType.INVENTORY_OPEN
    }

}