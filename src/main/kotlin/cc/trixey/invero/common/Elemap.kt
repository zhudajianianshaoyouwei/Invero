package cc.trixey.invero.common

/**
 * @author Arasple
 * @since 2022/12/29 13:50
 */
interface Elemap {

    fun addElement(element: ElementStatic, pos: Pos)

    fun addElement(element: ElementDynamic)

}