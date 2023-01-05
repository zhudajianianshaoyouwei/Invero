package cc.trixey.invero.common

/**
 * @author Arasple
 * @since 2022/12/29 13:51
 */
class ElementMap : Elemap {

    internal val elementStatic = hashMapOf<ElementStatic, Positions>()

    private val elementDynamic = mutableSetOf<ElementDynamic>()

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

    fun find(element: Element): Positions? {
        return elementStatic[element] ?: elementDynamic.find { it == element }?.getDynamicPositions().also {
            println(
                """
                    ---
                    Notfound in static
                    ${elementStatic.size}
                    ${elementStatic[element]}
                """.trimIndent()
            )
        }
    }

    override fun addElement(element: ElementStatic, pos: Pos) {
        elementStatic.computeIfAbsent(element) { Positions() } += pos
    }

    override fun addElement(element: ElementDynamic) {
        elementDynamic += element
    }

}