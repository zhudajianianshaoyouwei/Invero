package cc.trixey.invero.common

/**
 * @author Arasple
 * @since 2023/1/5 12:21
 */
interface IScale {

    val width: Int

    val height: Int

    val size: Int
        get() = width * height

    fun toSlot(x: Int, y: Int, index: Pos = Pos(0, 0)): Int

    fun toPosition(slot: Int): Pair<Int, Int>

    fun toPos(slot: Int) = Pos(toPosition(slot))

    fun toArea(index: Pos = Pos(0, 0)): Set<Pos> {
        val pos = mutableSetOf<Pos>()
        for (x in 0 until width) {
            for (y in 0 until height) {
                pos += Pos(index.x + x to index.y + y)
            }
        }

        return pos
    }

}