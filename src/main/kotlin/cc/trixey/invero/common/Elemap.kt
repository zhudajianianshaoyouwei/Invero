package cc.trixey.invero.common

/**
 * @author Arasple
 * @since 2022/12/29 13:50
 */
interface Elemap {

    fun setElement(pos: Pos, element: ElementStatic)

    fun addElement(element: ElementDynamic)

}