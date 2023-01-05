package cc.trixey.invero.common.event

import cc.trixey.invero.common.Viewer
import cc.trixey.invero.common.Window

/**
 * @author Arasple
 * @since 2023/1/5 13:16
 */
interface WindowEvent {

    val viewer: Viewer

    val window: Window

    val type: EventType

}