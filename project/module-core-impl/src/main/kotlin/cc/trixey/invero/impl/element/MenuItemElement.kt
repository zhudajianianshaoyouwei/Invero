package cc.trixey.invero.impl.element

import cc.trixey.invero.bukkit.element.item.SimpleItem
import cc.trixey.invero.common.Panel
import cc.trixey.invero.core.item.MenuElement
import cc.trixey.invero.core.menu.PanelAgent
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

/**
 * Invero
 * cc.trixey.invero.impl.element.MenuItemElement
 *
 * @author Arasple
 * @since 2023/1/15 14:05
 */
class MenuItemElement(
    val panelAgent: PanelAgent<*>,
    panel: Panel,
    itemStack: ItemStack = ItemStack(Material.AIR),
    var subIconIndex: Int = -1
) : SimpleItem(panel, itemStack), MenuElement