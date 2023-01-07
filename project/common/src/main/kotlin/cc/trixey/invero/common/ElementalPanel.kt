package cc.trixey.invero.common

/**
 * @author Arasple
 * @since 2022/12/30 12:39
 */
interface ElementalPanel : Panel {

    fun getElements(): Elements

    fun getUnoccupiedPositions(): Set<Pos> {
        return scale.toArea() - getElements().occupiedPositions()
    }

    override fun render() {
        getElements().forEach { push() }
    }

}