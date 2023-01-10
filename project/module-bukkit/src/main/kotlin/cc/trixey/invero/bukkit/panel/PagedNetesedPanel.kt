package cc.trixey.invero.bukkit.panel

import cc.trixey.invero.bukkit.BukkitPanel
import cc.trixey.invero.common.Element
import cc.trixey.invero.common.Panel
import cc.trixey.invero.common.Pos
import cc.trixey.invero.common.Scale
import cc.trixey.invero.common.event.WindowClickEvent
import cc.trixey.invero.common.panel.PagedPanel
import cc.trixey.invero.common.panel.PanelContainer
import cc.trixey.invero.common.panel.PanelWeight

/**
 * Invero
 * cc.trixey.invero.bukkit.panel.PagedNetesedPanel
 *
 * @author Arasple
 * @since 2023/1/9 16:04
 */
open class PagedNetesedPanel(
    parent: PanelContainer,
    weight: PanelWeight,
    scale: Scale,
    locate: Pos
) : BukkitPanel(parent, weight, scale, locate), PagedPanel, PanelContainer {

    override val panels = arrayListOf<Panel>()

    override val maxPageIndex: Int
        get() = panels.lastIndex

    override var pageIndex: Int = 0
        set(value) {
            if (value < 0) error("Page index can not be a negative number")
            else if (pageIndex > panels.indices.last) error("Page index is out of bounds ${panels.indices}")
            field = value

            render()
        }

    private val currentPanel: Panel
        get() = panels[pageIndex]

    override fun render() = currentPanel.rerender()

    override fun handleClick(pos: Pos, e: WindowClickEvent) = currentPanel.handleClick(pos, e)

    override fun isElementValid(element: Element) = currentPanel.isElementValid(element)

}