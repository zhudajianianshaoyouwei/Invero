package cc.trixey.invero.bukkit.event

import cc.trixey.invero.bukkit.BukkitViewer
import cc.trixey.invero.common.Window
import cc.trixey.invero.common.event.EventType
import cc.trixey.invero.common.event.WindowOpenEvent

/**
 * Invero
 * cc.trixey.invero.bukkit.event.PacketWindowOpenEvent
 *
 * @author Arasple
 * @since 2023/1/12 18:18
 */
class PacketWindowOpenEvent(
    viewer: BukkitViewer,
    window: Window
) : PacketWindowEvent(viewer, window, EventType.INVENTORY_OPEN), WindowOpenEvent {

    override val allowCancelled: Boolean
        get() = true

}