package cc.trixey.invero.bukkit.panel

import cc.trixey.invero.bukkit.BukkitPanel
import cc.trixey.invero.bukkit.element.Clickable
import cc.trixey.invero.common.Elements
import cc.trixey.invero.common.Pos
import cc.trixey.invero.common.Scale
import cc.trixey.invero.common.event.WindowClickEvent
import cc.trixey.invero.common.panel.ElementalPanel
import cc.trixey.invero.common.panel.PanelContainer
import cc.trixey.invero.common.panel.PanelWeight

/**
 * Invero
 * cc.trixey.invero.bukkit.panel.StandardPanel
 *
 * @author Arasple
 * @since 2022/12/22 20:32
 */
open class StandardPanel(
    parent: PanelContainer,
    weight: PanelWeight,
    scale: Scale,
    locate: Pos
) : BukkitPanel(parent, weight, scale, locate), ElementalPanel {

    override val elements = Elements()

    override fun handleClick(pos: Pos, e: WindowClickEvent): Boolean {
        elements.findElement(pos)?.let {
            if (it is Clickable<*>) {
                it.runHandler(e)
                return true
            }
        }
        return false
    }

}