package cc.trixey.invero.common

/**
 * @author Arasple
 * @since 2023/1/4 22:41
 */
@JvmInline
value class Scale(private val value: Pair<Int, Int>) : ScaleInterface {

    override val width: Int
        get() = value.first

    override val height: Int
        get() = value.second

    override fun isOutOfBounds(x: Int, y: Int, index: Pos): Boolean {
        val (rX, rY) = x + index.x to y + index.y

        return rX < 0 || rY < 0 || rX >= width || rY >= height
    }

    override fun toSlot(x: Int, y: Int, index: Pos): Int {
        return (y + index.y) * width + (x + index.x)
    }

    override fun toPosition(slot: Int): Pair<Int, Int> {
        val x = slot % width
        val y = (slot - x) / width

        return x to y
    }

}