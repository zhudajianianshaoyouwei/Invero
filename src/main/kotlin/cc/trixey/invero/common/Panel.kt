package cc.trixey.invero.common

import cc.trixey.invero.common.event.WindowClickEvent

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

    /**
     * Scale area related to its locate
     */
    val area: Set<Pos>
        get() = scale.toArea(locate)

    /**
     * Render this panel
     */
    fun render()

    /**
     * Wipe this panel
     */
    fun wipe() = wipe(area)

    fun wipe(pos: Set<Pos>) {
        if (parent.isPanel()) return parent.cast<Panel>().wipe(pos)

        window.let { window ->
            val slots = pos.map { it.toSlot(window.scale) }.toSet()
            window.inventory.clear(slots)
        }
    }

    fun rerender() = wipe().also { render() }

    /**
     * Event handler
     */
    fun handleClick(pos: Pos, e: WindowClickEvent)

}