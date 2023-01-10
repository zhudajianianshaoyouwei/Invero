package cc.trixey.invero.common.event

import cc.trixey.invero.common.Viewer
import cc.trixey.invero.common.Window

/**
 * Invero
 * cc.trixey.invero.common.event.WindowEvent
 *
 * @author Arasple
 * @since 2023/1/5 13:16
 */
interface WindowEvent {

    fun getViewer(): Viewer

    fun getWindow(): Window

    fun getType(): EventType

}