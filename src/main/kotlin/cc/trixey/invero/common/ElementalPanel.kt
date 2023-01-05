package cc.trixey.invero.common

/**
 * @author Arasple
 * @since 2022/12/30 12:39
 */
interface ElementalPanel : Panel {

    fun getElemap(): ElementMap

    fun getUnoccupiedPositions(): Set<Pos> {
        return scale.toArea() - getElemap().occupiedPositions()
    }

    fun wipe(pos: Set<Pos>) {
        window.let { window ->
            val slots = pos.map { it.toSlot(window.scale) }.toSet()
            window.inventory.clear(slots)
        }
    }

    override fun render() {
        getElemap().forEach { push() }
    }

}