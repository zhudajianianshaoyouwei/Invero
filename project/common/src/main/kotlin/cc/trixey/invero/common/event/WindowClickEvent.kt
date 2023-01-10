package cc.trixey.invero.common.event

import cc.trixey.invero.common.Viewer

/**
 * Invero
 * cc.trixey.invero.common.event.WindowClickEvent
 *
 * @author Arasple
 * @since 2023/1/6 11:47
 */
interface WindowClickEvent : WindowEvent, Cancellable {

    val clicker: Viewer

    val rawSlot: Int

    val clickType: ClickType

}