package cc.trixey.invero.common.event

import cc.trixey.invero.bukkit.BukkitViewer
import cc.trixey.invero.common.Window

/**
 * @author Arasple
 * @since 2023/1/5 14:01
 */
class PacketWindowEvent(override val type: EventType, override val window: Window, override val viewer: BukkitViewer) :
    WindowEvent {

}