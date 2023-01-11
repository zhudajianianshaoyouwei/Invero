package cc.trixey.invero.bukkit.panel

import cc.trixey.invero.bukkit.ProxyBukkitInventory
import cc.trixey.invero.common.Pos
import cc.trixey.invero.common.Scale
import cc.trixey.invero.common.event.WindowClickEvent
import cc.trixey.invero.common.panel.IOPanel
import cc.trixey.invero.common.panel.PanelContainer
import cc.trixey.invero.common.panel.PanelWeight

/**
 * Invero
 * cc.trixey.invero.bukkit.panel.IOStoragePanel
 *
 * @author Arasple
 * @since 2023/1/11 17:47
 */
class IOStoragePanel(
    parent: PanelContainer,
    weight: PanelWeight,
    scale: Scale,
    locate: Pos
) : StandardPanel(parent, weight, scale, locate), IOPanel {

    override val inventory: ProxyBukkitInventory
        get() = window.inventory as ProxyBukkitInventory

    override fun handleClick(pos: Pos, e: WindowClickEvent): Boolean {
        if (!super.handleClick(pos, e)) {
            println("Clicked at $pos")
            e.isCancelled = false

            return true
        }
        return false
    }

}