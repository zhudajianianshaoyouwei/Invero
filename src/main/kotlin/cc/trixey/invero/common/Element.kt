package cc.trixey.invero.common

/**
 * @author Arasple
 * @since 2022/12/22 20:23
 */
interface Element {

    val panel: Panel

    fun push()

    fun locatingSlot(pos: Pos): Int {
        var previous = panel
        var destination = previous.parent
        var result = pos

        while (destination.isPanel()) {
            result = pos.advance(previous.scale, destination.scale)
            previous = destination as Panel
            destination = previous.parent
        }
        return result.toSlot(destination.scale, previous.locate)
    }

}