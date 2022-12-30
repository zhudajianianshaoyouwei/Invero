package cc.trixey.invero.common

/**
 * @author Arasple
 * @since 2022/12/29 13:54
 */
@JvmInline
value class Positions(private val values: MutableSet<Pos> = mutableSetOf()) {

    fun toLocations(): List<Pair<Int, Int>> {
        return values.filter { !it.isPureSlot }.map { it.locate }
    }

    fun add(x: Int, y: Int) {
        values += Pos(x, y)
    }

    fun add(pos: Pos) {
        values += pos
    }

    fun add(slot: Int) {
        values += Pos(slot)
    }

    fun has(slot: Int): Boolean {
        return values.find { it.isPureSlot && it.pureSlot == slot } != null
    }

    fun has(x: Int, y: Int): Boolean {
        return values.find { !it.isPureSlot && it.locate == x to y } != null
    }

    fun remove(slot: Int) {
        values.removeIf { it.isPureSlot && it.pureSlot == slot }
    }

    fun remove(x: Int, y: Int) {
        values.removeIf { !it.isPureSlot && it.locate == x to y }
    }

}