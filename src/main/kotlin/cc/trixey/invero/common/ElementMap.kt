package cc.trixey.invero.common

/**
 * @author Arasple
 * @since 2022/12/29 13:51
 */
class ElementMap : Elemap {

    internal val elementStatic = hashMapOf<ElementStatic, Positions>()

    private val elementDynamic = mutableSetOf<ElementDynamic>()

    fun occupiedPositions(): Set<Pos> {
        return elementStatic.values.flatMap { it.values }.toSet()
    }

    fun forEachDynamic(block: ElementDynamic.() -> Unit) {
        elementDynamic.forEach(block)
    }

    fun forEachStatic(block: ElementStatic.(Positions) -> Unit) {
        elementStatic.forEach { it.key.block(it.value) }
    }

    fun forEach(block: Element.() -> Unit) {
        elementStatic.forEach { it.key.block() }
        elementDynamic.forEach(block)
    }

    fun locate(element: Element): Positions? {
        return elementStatic[element] ?: elementDynamic.find { it == element }?.getDynamicPositions()
    }

    fun find(pos: Pos): Element? {
        return elementStatic.entries.firstOrNull { pos in it.value }?.key ?: elementDynamic.find { pos in it.getDynamicPositions() }
    }

    override fun addElement(element: ElementStatic, pos: Pos) {
        elementStatic.computeIfAbsent(element) { Positions() } += pos
    }

    override fun addElement(element: ElementDynamic) {
        elementDynamic += element
    }

}