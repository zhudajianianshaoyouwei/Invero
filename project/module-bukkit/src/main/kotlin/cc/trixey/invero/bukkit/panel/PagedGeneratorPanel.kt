package cc.trixey.invero.bukkit.panel

import cc.trixey.invero.bukkit.BukkitPanel
import cc.trixey.invero.common.Element
import cc.trixey.invero.common.Pos
import cc.trixey.invero.common.Scale
import cc.trixey.invero.common.event.WindowClickEvent
import cc.trixey.invero.common.panel.PagedPanel
import cc.trixey.invero.common.panel.PanelContainer
import cc.trixey.invero.common.panel.PanelWeight

/**
 * Invero
 * cc.trixey.invero.bukkit.panel.PagedGeneratorPanel
 *
 * @author Arasple
 * @since 2023/1/10 20:50
 */
class PagedGeneratorPanel(
    parent: PanelContainer,
    weight: PanelWeight,
    scale: Scale,
    locate: Pos
) : BukkitPanel(parent, weight, scale, locate), PagedPanel {

    override val maxPageIndex: Int
        get() = TODO("Not yet implemented")
    override var pageIndex: Int
        get() = TODO("Not yet implemented")
        set(value) {}

    override fun render() {
        TODO("Not yet implemented")
    }

    override fun isElementValid(element: Element): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleClick(pos: Pos, e: WindowClickEvent) {
        TODO("Not yet implemented")
    }

}