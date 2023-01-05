package cc.trixey.invero.bukkit.panel

import cc.trixey.invero.bukkit.BukkitPanel
import cc.trixey.invero.common.*
import org.bukkit.event.inventory.InventoryClickEvent

/**
 * @author Arasple
 * @since 2023/1/5 20:49
 */
class PagedStandardPanel(
    parent: PanelContainer,
    weight: PanelWeight,
    scale: Scale,
    locate: Pos
) : BukkitPanel(parent, weight, scale, locate), ElementalPanel {

    private val elements: ElementMap = ElementMap()

    override fun handleClick(pos: Pos, e: InventoryClickEvent) {
        TODO("Not yet implemented")
    }

    override fun getElemap(): ElementMap {
        TODO("Not yet implemented")
    }

}