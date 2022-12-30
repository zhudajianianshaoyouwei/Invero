package cc.trixey.invero.common

/**
 * @author Arasple
 * @since 2022/12/22 20:23
 */
interface Element {

    val panel: Panel

    fun push(parent: PanelContainer = panel.parent)

}