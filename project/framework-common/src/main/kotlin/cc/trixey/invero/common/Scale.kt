package cc.trixey.invero.common

/**
 * Invero
 * cc.trixey.invero.common.Scale
 *
 * @author Arasple
 * @since 2023/1/4 22:41
 */
@JvmInline
value class Scale(val raw: Pair<Int, Int>) {

    val width: Int
        get() = raw.first

    val height: Int
        get() = raw.second

    val size: Int
        get() = width * height

    fun isOutOfBounds(x: Int, y: Int, index: Pos = Pos.NIL): Boolean {
        val (rX, rY) = x + index.x to y + index.y

        return rX < 0 || rY < 0 || rX >= width || rY >= height
    }

    fun convertToSlot(x: Int, y: Int, index: Pos = Pos.NIL): Int {
        return (y + index.y) * width + (x + index.x)
    }

    fun convertToSlot(pos: Pos, index: Pos = Pos.NIL): Int {
        return convertToSlot(pos.x, pos.y, index)
    }

    fun convertToPosition(slot: Int): Pos {
        val x = slot % width
        val y = (slot - x) / width

        return Pos(x to y)
    }

    fun getArea(index: Pos = Pos.NIL): Set<Pos> {
        val pos = mutableSetOf<Pos>()
        for (x in 0 until width) {
            for (y in 0 until height) {
                pos += Pos(index.x + x to index.y + y)
            }
        }

        return pos
    }

    fun coerceIn(scale: Scale): Scale {
        return if (width <= scale.width && height <= scale.height) this
        else Scale(width.coerceAtMost(scale.width) to height.coerceAtMost(scale.height))
    }

    override fun toString(): String {
        return "[$width x $height]"
    }

}