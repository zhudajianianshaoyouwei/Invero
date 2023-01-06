package cc.trixey.invero.bukkit.event

import cc.trixey.invero.common.Viewer
import cc.trixey.invero.common.Window
import cc.trixey.invero.common.event.ClickType
import cc.trixey.invero.common.event.EventType
import cc.trixey.invero.common.event.WindowClickEvent

/**
 * @author Arasple
 * @since 2023/1/6 11:45
 */
class PacketClickEvent(
    viewer: Viewer,
    window: Window,
    override var isCancelled: Boolean,
    override val rawSlot: Int,
    override val clickType: ClickType
) : PacketWindowEvent(viewer, window, EventType.CLICK), WindowClickEvent