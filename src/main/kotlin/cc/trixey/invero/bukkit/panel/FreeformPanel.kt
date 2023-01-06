package cc.trixey.invero.bukkit.panel

import cc.trixey.invero.bukkit.BukkitPanel
import cc.trixey.invero.common.*
import cc.trixey.invero.common.event.WindowClickEvent

/**
 * @author Arasple
 * @since 2022/12/29 13:48
 */
class FreeformPanel(
    parent: PanelContainer,
    weight: PanelWeight,
    override val scale: ScaleViewport,
    locate: Pos
) : BukkitPanel(parent, weight, scale, locate), ElementalPanel {

    private val elements: Elements = Elements()

    override fun getElements(): Elements {
        return elements
    }

    var viewport: Pos = Pos(0 to 0)
        set(value) {
            field = value
            scale.viewport = value
            rerender()
        }

    override fun handleClick(pos: Pos, e: WindowClickEvent) {
        getElements().findElement(pos)?.let {
            if (it is Clickable) {
                it.passClickEvent(e)
            }
        }
    }

}