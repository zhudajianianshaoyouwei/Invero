package cc.trixey.invero.common

/**
 * @author Arasple
 * @since 2022/12/20 20:43
 */
interface Panel {

    /**
     * The parent of this panel
     *
     * e.g.
     * Window
     * Netesed or PanelGroup
     */
    val parent: PanelContainer

    /**
     * The weight of this panel
     */
    val weight: PanelWeight

    /**
     * The scale of this panel
     *
     * Width * Height
     */
    val scale: Pair<Int, Int>

    /**
     * The location of this panel relative to its parent
     */
    val locate: Pos

}