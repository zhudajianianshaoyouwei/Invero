package cc.trixey.invero.common

/**
 * @author Arasple
 * @since 2022/12/29 22:16
 */
@JvmInline
value class Pos(private val value: IntArray) {

    /**
     * 纯 Slot (<100)
     * 1 000 023
     */
    constructor(slot: Int) : this(IntArray(7) { 0 }.apply {
        this[0] = 1

        var lastIndex = 6
        val digits = slot.toString().toCharArray().map { it.digitToInt() }.reversed()
        digits.forEach { this[lastIndex--] = it }
    })

    /**
     * 行，列 (x/y）定位
     * 0 010 045
     */
    constructor(x: Int, y: Int) : this(IntArray(7) { 0 }.apply {
        this[0] = 0

        x.toString().toCharArray().let { for (index in 0..2) this[index + 1] = it[index].digitToInt() }
        y.toString().toCharArray().let { for (index in 0..2) this[index + 4] = it[index].digitToInt() }
    })

    val isPureSlot: Boolean
        get() = value[0] == 1

    val pureSlot: Int
        // Slot>100 的情况暂时忽略
        get() = value[5] * 10 + value[6]

    val locate: Pair<Int, Int>
        get() = value[1] * 100 + value[2] * 10 + value[3] to value[4] * 100 + value[5] * 10 + value[6]

}