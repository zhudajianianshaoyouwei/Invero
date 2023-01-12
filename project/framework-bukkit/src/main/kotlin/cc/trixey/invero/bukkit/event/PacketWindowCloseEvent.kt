package cc.trixey.invero.bukkit.event

import cc.trixey.invero.bukkit.BukkitViewer
import cc.trixey.invero.common.Window
import cc.trixey.invero.common.event.EventType
import cc.trixey.invero.common.event.WindowCloseEvent

/**
 * Invero
 * cc.trixey.invero.bukkit.event.PacketWindowCloseEvent
 *
 * @author Arasple
 * @since 2023/1/12 18:18
 */
class PacketWindowCloseEvent(
    viewer: BukkitViewer,
    window: Window
) : PacketWindowEvent(viewer, window, EventType.INVENTORY_CLOSE), WindowCloseEvent