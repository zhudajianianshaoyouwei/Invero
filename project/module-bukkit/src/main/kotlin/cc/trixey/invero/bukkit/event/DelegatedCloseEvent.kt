package cc.trixey.invero.bukkit.event

import cc.trixey.invero.bukkit.BukkitViewer
import cc.trixey.invero.bukkit.BukkitWindowHolder
import cc.trixey.invero.common.Viewer
import cc.trixey.invero.common.Window
import cc.trixey.invero.common.event.EventType
import cc.trixey.invero.common.event.WindowCloseEvent
import org.bukkit.event.inventory.InventoryCloseEvent

/**
 * Invero
 * cc.trixey.invero.bukkit.event.DelegatedCloseEvent
 *
 * @author Arasple
 * @since 2023/1/6 11:53
 */
@JvmInline
value class DelegatedCloseEvent(
    override val event: InventoryCloseEvent
) : WindowCloseEvent, DelegatedInventoryEvent {

    override fun getViewer(): Viewer {
        return BukkitViewer(event.player.uniqueId)
    }

    override fun getWindow(): Window {
        return (event.inventory.holder as BukkitWindowHolder).window
    }

    override fun getType(): EventType {
        return EventType.INVENTORY_CLOSE
    }

}