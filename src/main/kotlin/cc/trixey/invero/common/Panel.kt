package cc.trixey.invero.common

/**
 * @author Arasple
 * @since 2022/12/20 20:43
 */
interface Panel : Gridable {

    /**
     * The parent of this panel
     *
     * e.g.
     * Window
     * Netesed or PanelGroup
     */
    val parent: PanelContainer

    /**
     * The window that hold this panel
     */
    val window: Window

    /**
     * The weight of this panel
     */
    val weight: PanelWeight

    /**
     * The location of this panel relative to its parent
     */
    val locate: Pos

    val area: Set<Pos>
        get() = scale.toArea(locate)

    /**
     * Render this panel
     */
    fun render()

    /**
     * Wipe this panel
     */
    fun wipe()

}