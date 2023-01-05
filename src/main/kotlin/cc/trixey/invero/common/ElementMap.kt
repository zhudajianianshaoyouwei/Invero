package cc.trixey.invero.common

/**
 * @author Arasple
 * @since 2022/12/29 13:51
 */
@JvmInline
value class ElementMap(val elements: HashMap<Element, Positions> = hashMapOf()) {

    fun occupiedPositions(): Set<Pos> {
        return elements.values.flatMap { it.values }.toSet()
    }

    fun forEach(block: Element.() -> Unit) {
        elements.forEach { it.key.block() }
    }

    fun locateElement(element: Element): Positions? {
        return elements[element]
    }

    fun findElement(pos: Pos): Element? {
        return elements.entries.firstOrNull { pos in it.value }?.key
    }

    fun addElement(element: Element, pos: Pos) {
        elements.computeIfAbsent(element) { Positions() } += pos
    }

    fun setElement(element: Element, positions: Positions): Set<Pos>? {
        val removable = locateElement(element)?.minus(positions)
        elements[element] = positions
        return removable
    }

}