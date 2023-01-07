package cc.trixey.invero.bukkit.event

import cc.trixey.invero.common.Viewer
import cc.trixey.invero.common.Window
import cc.trixey.invero.common.event.EventType
import cc.trixey.invero.common.event.WindowEvent

/**
 * @author Arasple
 * @since 2023/1/6 11:38
 */
abstract class PacketWindowEvent(
    private val viewer: Viewer,
    private val window: Window,
    private val type: EventType
) : WindowEvent {

    override fun getViewer(): Viewer {
        return viewer
    }

    override fun getWindow(): Window {
        return window
    }

    override fun getType(): EventType {
        return type
    }

}