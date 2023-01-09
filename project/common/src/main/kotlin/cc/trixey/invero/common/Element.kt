package cc.trixey.invero.common

import cc.trixey.invero.common.panel.FreeformPanel

/**
 * @author Arasple
 * @since 2022/12/22 20:23
 */
interface Element {

    val panel: Panel

    fun postRender(block: (Pos) -> Unit)

    fun push()

    fun safePush() {
        if (shouldPush()) push()
    }

    fun shouldPush() = panel.window.hasViewer()

    fun Pos.locatingSlot(): Int {
        val layout = panel.scale
        var position = this
        var current = panel
        var forward = current.parent

        // 可视度检查
        if (current is FreeformPanel) {
            val viewport = -current.viewport

            if (layout.isOutOfBounds(x, y, viewport)) return -1
            position += viewport
        } else if (layout.isOutOfBounds(x, y)) return -1

        // 逐层寻找父级
        while (forward.isPanel()) {
            position = position.advance(current.scale, forward.scale)
            current = forward as Panel
            forward = current.parent
        }

        // 返回相对窗口（最高层）的槽位
        return position.convertToSlot(forward.scale, current.locate)
    }

}