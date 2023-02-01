package cc.trixey.invero.common.supplier

/**
 * Invero
 * cc.trixey.invero.common.supplier.ElementGenerator
 *
 * @author Arasple
 * @since 2023/2/1 16:45
 */
interface ElementGenerator {

    var generated: List<Object>?

    fun generate(): List<Object>

    fun filter(block: (Object) -> Boolean): ElementGenerator

    fun <R : Comparable<R>> sortBy(block: (Object) -> R): ElementGenerator

}