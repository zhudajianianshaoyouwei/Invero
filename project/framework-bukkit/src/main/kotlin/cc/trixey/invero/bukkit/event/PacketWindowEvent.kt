package cc.trixey.invero.bukkit.event

import cc.trixey.invero.bukkit.BukkitViewer
import cc.trixey.invero.common.Window
import cc.trixey.invero.common.event.EventType
import cc.trixey.invero.common.event.WindowEvent
import taboolib.platform.type.BukkitProxyEvent

/**
 * Invero
 * cc.trixey.invero.bukkit.event.PacketWindowEvent
 *
 * @author Arasple
 * @since 2023/1/6 11:38
 */
abstract class PacketWindowEvent(
    override val viewer: BukkitViewer,
    override val window: Window,
    override val type: EventType
) : WindowEvent, BukkitProxyEvent() {

    override val allowCancelled: Boolean
        get() = false

}