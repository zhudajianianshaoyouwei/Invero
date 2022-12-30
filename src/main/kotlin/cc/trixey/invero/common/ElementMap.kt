package cc.trixey.invero.common

/**
 * @author Arasple
 * @since 2022/12/29 13:51
 */
class ElementMap : Elemap {

    private val elementStatic = hashMapOf<ElementStatic, Positions>()

    private val elementDynamic = mutableSetOf<ElementDynamic>()

    fun forEachDynamic(block: ElementDynamic.() -> Unit) {
        elementDynamic.forEach(block)
    }

    fun forEachStatic(block: ElementStatic.(Positions) -> Unit) {
        elementStatic.forEach { it.key.block(it.value) }
    }

    fun find(element: Element): Positions? {
        return elementStatic[element] ?: elementDynamic.find { it == element }?.getDynamicPositions()
    }

    override fun setElement(pos: Pos, element: ElementStatic) {
        elementStatic.computeIfAbsent(element) { Positions() }.add(pos)
    }

    override fun addElement(element: ElementDynamic) {
        elementDynamic += element
    }

}