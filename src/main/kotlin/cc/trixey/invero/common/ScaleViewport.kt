package cc.trixey.invero.common

/**
 * @author Arasple
 * @since 2023/1/5 12:24
 */
data class ScaleViewport(var viewport: Pos, private val value: Pair<Int, Int>) : ScaleInterface {

    override val width: Int
        get() = value.first

    override val height: Int
        get() = value.second

    override fun toSlot(x: Int, y: Int, index: Pos): Int {
        val (rX, rY) = x + index.x to y + index.y

        return if (rX >= viewport.x + width || rY >= viewport.y + height) -1
        else (rY - viewport.y) * height + (rX - viewport.x)
    }

    override fun toPosition(slot: Int): Pair<Int, Int> {
        val x = slot % width - viewport.x
        val y = (slot - x) / width - 1 - viewport.y

        return x to y
    }

}