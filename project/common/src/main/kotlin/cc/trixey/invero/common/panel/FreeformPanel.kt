package cc.trixey.invero.common.panel

import cc.trixey.invero.common.Panel
import cc.trixey.invero.common.Pos

/**
 * Invero
 * cc.trixey.invero.common.panel.FreeformPanel
 *
 * @author Arasple
 * @since 2023/1/6 14:57
 */
interface FreeformPanel : Panel {

    val viewport: Pos

    fun toAbsolutePosition(slot: Int): Pos {
        return viewport + scale.convertToPosition(slot)
    }

    fun resetViewport() = setViewport(0, 0)

    fun setViewport(x: Int = 0, y: Int = 0)

    fun shift(x: Int = 0, y: Int = 0)

    fun shiftLeft() = shift(-1)

    fun shiftRight() = shift(1)

    fun shiftUp() = shift(y = -1)

    fun shiftDown() = shift(y = 1)

    fun shiftUpLeft() = shift(-1, -1)

    fun shiftUpRight() = shift(1, -1)

    fun shiftDownLeft() = shift(-1, 1)

    fun shiftDownRight() = shift(1, 1)

    val viewArea: List<Pos>
        get() = scale.getArea(locate).map { it + viewport }

    override fun wipe() = wipe(viewArea)

    /*
    FreeformPanel.wipe(args: Set<Pos>)

    args = absolute position
     */
    override fun wipe(wiping: Collection<Pos>) = wiping
        .map { it - viewport }
        .filterNot { scale.isOutOfBounds(it.x, it.y) }
        .let { super.wipe(it) }

}