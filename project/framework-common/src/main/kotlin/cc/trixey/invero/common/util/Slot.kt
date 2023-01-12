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

    if (panel is FreeformPanel) {
        pos -= panel.viewport

        if (panel.scale.isOutOfBounds(pos.x, pos.y)) {
            return -1
        }
    }

    return if (parent.isWindow()) {
        pos.convertToSlot(parent.scale, panel.locate)
    } else if (parent.isFreeform()) {
        pos += panel.locate

        locatingAbsoluteSlot(pos, parent as Panel)
    } else {
        parent as Panel

        val parentPos = pos.convertToParent(panel.locate)
        locatingAbsoluteSlot(parentPos, parent)
    }
}