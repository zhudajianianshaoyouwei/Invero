package cc.trixey.invero.common.panel

import cc.trixey.invero.common.Panel
import cc.trixey.invero.common.Pos

/**
 * @author Arasple
 * @since 2023/1/6 14:57
 */
interface FreeformPanel : Panel {

    val viewport: Pos

    fun toAbsolutePosition(slot: Int): Pos {
        return scale.convertToPosition(slot) + viewport

    }

    fun resetViewport()

    fun shift(x: Int = 0, y: Int = 0)

    fun shiftLeft() = shift(-1)

    fun shiftRight() = shift(1)

    fun shiftUp() = shift(y = -1)

    fun shiftDown() = shift(y = 1)

    fun shiftUpLeft() = shift(-1, -1)

    fun shiftUpRight() = shift(1, -1)

    fun shiftDownLeft() = shift(-1, 1)

    fun shiftDownRight() = shift(1, 1)

    override fun wipe(wiping: Set<Pos>, absolute: Boolean) {
        run {
            if (!absolute) wiping
                .filterNot { scale.isOutOfBounds(it.x, it.y, -viewport) }
                .map { it - viewport }
                .toSet()
            else wiping
        }.let {
            super.wipe(it, absolute)
        }
    }

}