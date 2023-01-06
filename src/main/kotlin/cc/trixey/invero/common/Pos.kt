package cc.trixey.invero.common

/**
 * @author Arasple
 * @since 2023/1/4 22:41
 *
 * 槽位的准确坐标
 * 采用 (x,y) 计法
 *
 * 相对任意 Scale 运算可得到一个 Slot 值
 * 真实的 Slot 值只有最高层（Window）需要利用
 */
@JvmInline
value class Pos(internal val value: Pair<Int, Int>) {

    val x: Int
        get() = value.first

    val y: Int
        get() = value.second

    constructor(x: Int, y: Int) : this(x to y)

    constructor(slot: Int, scale: ScaleInterface) : this(scale.toPosition(slot))

    fun toSlot(scale: ScaleInterface, index: Pos = Pos(0, 0)): Int {
        return scale.toSlot(x, y, index)
    }

    fun advance(previous: ScaleInterface, destination: ScaleInterface): Pos {
        return Pos(destination.toPosition(previous.toSlot(x, y)))
    }

    operator fun minus(pos: Pos): Pos {
        return Pos(x - pos.x, y - pos.y)
    }

}