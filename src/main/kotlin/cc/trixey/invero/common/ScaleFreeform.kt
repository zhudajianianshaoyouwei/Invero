package cc.trixey.invero.common

/**
 * @author Arasple
 * @since 2023/1/5 12:24
 */
data class ScaleFreeform(var viewport: Pos, private val value: Pair<Int, Int>) : IScale {

    override val width: Int
        get() = value.first

    override val height: Int
        get() = value.second

    override fun toSlot(x: Int, y: Int): Int {
        return if (x >= viewport.x + width || y >= viewport.y + height) -1
        else (y - viewport.y) * height + (x - viewport.x)
    }

    override fun toPosition(slot: Int): Pair<Int, Int> {
        val x = slot % width - viewport.x
        val y = (slot - x) / width - 1 - viewport.y

        return x to y
    }

}