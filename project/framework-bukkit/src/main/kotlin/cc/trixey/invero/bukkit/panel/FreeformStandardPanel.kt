package cc.trixey.invero.bukkit.panel

import cc.trixey.invero.bukkit.BukkitPanel
import cc.trixey.invero.bukkit.element.Clickable
import cc.trixey.invero.common.Elements
import cc.trixey.invero.common.Pos
import cc.trixey.invero.common.Scale
import cc.trixey.invero.common.event.WindowClickEvent
import cc.trixey.invero.common.panel.ElementalPanel
import cc.trixey.invero.common.panel.FreeformPanel
import cc.trixey.invero.common.panel.PanelContainer
import cc.trixey.invero.common.panel.PanelWeight

/**
 * Invero
 * cc.trixey.invero.bukkit.panel.FreeformStandardPanel
 *
 * @author Arasple
 * @since 2022/12/29 13:48
 *
 * Element 的位置应使用 （x,y) 准确定义
 * FreeformPanel 应直接作为 Window 的子代，尽量不要嵌套其他父级 Panel，如果有BUG我也不想管了
 */
open class FreeformStandardPanel(
    parent: PanelContainer,
    weight: PanelWeight,
    scale: Scale,
    locate: Pos
) : BukkitPanel(parent, weight, scale, locate), ElementalPanel, FreeformPanel {

    override val elements = Elements()

    override var viewport: Pos = Pos.NIL
        set(value) {
            field = value
            rerender()
        }

    override fun handleClick(pos: Pos, e: WindowClickEvent): Boolean {
        elements.findElement(pos + viewport)?.let {
            if (it is Clickable<*>) {
                it.runHandler(e)
                return true
            }
        }
        return false
    }

}