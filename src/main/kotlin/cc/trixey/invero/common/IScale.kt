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

    fun toSlot(x: Int, y: Int): Int

    fun toPosition(slot: Int): Pair<Int, Int>

}