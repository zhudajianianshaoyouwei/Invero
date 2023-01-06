package cc.trixey.invero.bukkit.panel

import cc.trixey.invero.bukkit.BukkitPanel
import cc.trixey.invero.common.*
import cc.trixey.invero.common.event.WindowClickEvent
import java.util.*

/**
 * @author Arasple
 * @since 2023/1/5 20:49
 */
class PagedStandardPanel(
    parent: PanelContainer,
    weight: PanelWeight,
    scale: Scale,
    locate: Pos
) : BukkitPanel(parent, weight, scale, locate), ElementalPanel, PagedPanel {

    override var pageIndex: Int = 0
        set(value) {
            if (value <= 0) error("Page index can not be a negative number")
            else if (pageIndex > pagedElements.indices.last) error("Page index is out of bounds ${pagedElements.indices}")

            wipe()
            render()
            field = value
        }

    private val pagedElements: LinkedList<Elements> = LinkedList<Elements>()

    override fun getElements(): Elements {
        return pagedElements[pageIndex]
    }

    override fun handleClick(pos: Pos, e: WindowClickEvent) {
        getElements().findElement(pos)?.let {
            if (it is Clickable) {
                it.passClickEvent(e)
            }
        }
    }

}