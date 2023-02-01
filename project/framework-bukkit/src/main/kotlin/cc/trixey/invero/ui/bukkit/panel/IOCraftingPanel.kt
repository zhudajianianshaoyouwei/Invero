package cc.trixey.invero.ui.bukkit.panel

import cc.trixey.invero.ui.bukkit.PanelContainer
import cc.trixey.invero.ui.bukkit.util.proceed
import cc.trixey.invero.ui.common.Pos
import cc.trixey.invero.ui.common.Scale
import cc.trixey.invero.ui.common.event.ClickType
import cc.trixey.invero.ui.common.panel.IOPanel
import cc.trixey.invero.ui.common.panel.PanelWeight
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.inventory.ItemStack

/**
 * Invero
 * cc.trixey.invero.ui.bukkit.panel.IOCraftingPanel
 *
 * @author Arasple
 * @since 2023/1/11 17:48
 */
class IOCraftingPanel(
    parent: PanelContainer,
    weight: PanelWeight,
    scale: Scale,
    locate: Pos
) : IOStoragePanel(parent, weight, scale, locate), IOPanel {

    private var storageCallback: Array<ItemStack?>.() -> Unit = { }

    private fun callback() = storageCallback(storage)

    override fun insertItemStack(pos: Pos, itemStack: ItemStack): Boolean {
        return super.insertItemStack(pos, itemStack).proceed { callback() }
    }

    override fun stackItemStack(itemStack: ItemStack): Int {
        return super.stackItemStack(itemStack).proceed({ it != itemStack.amount }) { callback() }
    }


    override fun handleClick(pos: Pos, clickType: ClickType, e: InventoryClickEvent?): Boolean {
        return super.handleClick(pos, clickType, e).proceed { callback() }
    }

    override fun handleItemsMove(pos: Pos, e: InventoryClickEvent): Boolean {
        return super.handleItemsMove(pos, e).proceed { callback() }
    }

    override fun handleDrag(pos: List<Pos>, e: InventoryDragEvent): Boolean {
        return super.handleDrag(pos, e).proceed { callback() }
    }

    fun onStorageChange(block: Array<ItemStack?>.() -> Unit) {
        storageCallback = block
    }

}