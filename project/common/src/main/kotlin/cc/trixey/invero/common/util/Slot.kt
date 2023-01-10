package cc.trixey.invero.common.util

import cc.trixey.invero.common.Panel
import cc.trixey.invero.common.Pos
import cc.trixey.invero.common.panel.FreeformPanel

/**
 * 将一个归属于 Panel 的 Pos 递归到最高层 Window 的真实 Slot
 */
fun locatingAbsoluteSlot(position: Pos, panel: Panel): Int {
    val parent = panel.parent
    var pos = position

    // absolutePos 0,0 viewport = -1,0, viewPos should be 1,0
    if (panel is FreeformPanel) {
        pos -= panel.viewport

        if (panel.scale.isOutOfBounds(pos.x, pos.y)) {
            return -1
        }
    }

    return if (parent.isWindow()) {
        pos.convertToSlot(parent.scale, panel.locate)
    } else {
        parent as Panel

        val parentPos = pos.convertToParent(panel.scale, parent.scale, panel.locate)
        locatingAbsoluteSlot(parentPos, parent)
    }
}