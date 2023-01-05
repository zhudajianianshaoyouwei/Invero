package cc.trixey.invero.common

/**
 * @author Arasple
 * @since 2023/1/4 22:54
 */
@JvmInline
value class Positions(val values: MutableSet<Pos> = mutableSetOf()) {

    operator fun plusAssign(pos: Pos) {
        values += pos
    }

    operator fun contains(pos: Pos): Boolean {
        return pos in values
    }

    operator fun minus(positions: Positions?): Set<Pos> {
        return if (positions == null) values
        else values - positions.values
    }

}