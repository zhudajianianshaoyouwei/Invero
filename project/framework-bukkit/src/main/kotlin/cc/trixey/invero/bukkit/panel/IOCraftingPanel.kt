package cc.trixey.invero.bukkit.panel

import cc.trixey.invero.bukkit.util.proceed
import cc.trixey.invero.common.Pos
import cc.trixey.invero.common.Scale
import cc.trixey.invero.common.event.WindowClickEvent
import cc.trixey.invero.common.event.WindowItemsMoveEvent
import cc.trixey.invero.common.panel.IOPanel
import cc.trixey.invero.common.panel.PanelContainer
import cc.trixey.invero.common.panel.PanelWeight
import org.bukkit.inventory.ItemStack

/**
 * Invero
 * cc.trixey.invero.bukkit.panel.IOCraftingPanel
 *
 * @author Arasple
 * @since 2023/1/11 17:48
 */
class IOCraftingPanel(
    parent: PanelContainer, weight: PanelWeight, scale: Scale, locate: Pos
) : IOStoragePanel(parent, weight, scale, locate), IOPanel {

    private var storageCallback: Array<ItemStack?>.() -> Unit = { }

    private fun callback() = storageCallback(storage)

    override fun insertItemStack(pos: Pos, itemStack: ItemStack): Boolean {
        return super.insertItemStack(pos, itemStack).proceed { callback() }
    }

    override fun stackItemStack(itemStack: ItemStack): Int {
        return super.stackItemStack(itemStack).proceed({ it != itemStack.amount }) { callback() }
    }

    override fun handleClick(pos: Pos, e: WindowClickEvent): Boolean {
        return super.handleClick(pos, e).proceed { callback() }
    }

    override fun handleItemsMove(pos: Pos, e: WindowItemsMoveEvent): Boolean {
        return super.handleItemsMove(pos, e).proceed { callback() }
    }

    fun onItemsChange(block: Array<ItemStack?>.() -> Unit) {
        storageCallback = block
    }

}