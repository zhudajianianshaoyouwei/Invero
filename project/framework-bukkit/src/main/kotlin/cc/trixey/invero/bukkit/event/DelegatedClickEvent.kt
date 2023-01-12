package cc.trixey.invero.bukkit.event

import cc.trixey.invero.bukkit.BukkitViewer
import cc.trixey.invero.bukkit.BukkitWindowHolder
import cc.trixey.invero.common.Viewer
import cc.trixey.invero.common.Window
import cc.trixey.invero.common.event.ClickType
import cc.trixey.invero.common.event.EventType
import cc.trixey.invero.common.event.WindowClickEvent
import org.bukkit.event.inventory.InventoryClickEvent

/**
 * Invero
 * cc.trixey.invero.bukkit.event.DelegatedClickEvent
 *
 * @author Arasple
 * @since 2023/1/6 11:47
 */
@JvmInline
value class DelegatedClickEvent(
    override val event: InventoryClickEvent
) : WindowClickEvent, DelegatedInventoryEvent {

    override val viewer: Viewer
        get() = BukkitViewer(event.whoClicked.uniqueId)

    override val window: Window
        get() = (event.whoClicked.openInventory.topInventory.holder as BukkitWindowHolder).window

    override val type: EventType
        get() = EventType.CLICK

    override val rawSlot: Int
        get() = event.rawSlot

    override var clickCancelled: Boolean
        get() = event.isCancelled
        set(value) {
            event.isCancelled = value
        }

    override val clickType: ClickType
        get() = ClickType.findBukkit(event.click.name)!!

}