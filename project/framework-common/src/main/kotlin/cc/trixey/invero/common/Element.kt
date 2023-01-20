package cc.trixey.invero.common

/**
 * Invero
 * cc.trixey.invero.common.Element
 *
 * @author Arasple
 * @since 2022/12/22 20:23
 */
interface Element {

    val panel: Panel

    fun postRender(block: (Pos) -> Unit)

    fun push()

    fun safePush() {
        if (panel.window.isViewing()) push()
    }

}