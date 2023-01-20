package cc.trixey.invero.bukkit.panel

import cc.trixey.invero.bukkit.PanelContainer
import cc.trixey.invero.bukkit.ProxyBukkitInventory
import cc.trixey.invero.bukkit.util.reachedMaxStackSize
import cc.trixey.invero.common.Pos
import cc.trixey.invero.common.Scale
import cc.trixey.invero.common.event.ClickType
import cc.trixey.invero.common.panel.ElementalPanel
import cc.trixey.invero.common.panel.IOPanel
import cc.trixey.invero.common.panel.PanelWeight
import cc.trixey.invero.common.util.locatingAbsoluteSlot
import org.bukkit.event.inventory.InventoryAction
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.inventory.ItemStack
import taboolib.common.platform.function.submit
import taboolib.platform.util.isAir

/**
 * Invero
 * cc.trixey.invero.bukkit.panel.IOStoragePanel
 *
 * @author Arasple
 * @since 2023/1/11 17:47
 *
 * IOPanel 应该作为一个直接隶属于 BukkitWindow、不包含动态槽位更改的 Panel
 * 否则 90% 会触发神秘 BUG
 */
open class IOStoragePanel(
    parent: PanelContainer,
    weight: PanelWeight,
    scale: Scale,
    locate: Pos
) : StandardPanel(parent, weight, scale, locate), IOPanel, ElementalPanel {

    override val inventory: ProxyBukkitInventory
        get() = window.inventory as ProxyBukkitInventory

    protected val storage = arrayOfNulls<ItemStack?>(scale.size)

    private var storageCallback: Array<ItemStack?>.() -> Unit = { }

    override val insertable: List<Pos> by lazy { (area - locked.toSet()).sorted() }

    override val locked: List<Pos> by lazy { elements.occupiedPositions().sorted() }

    override fun renderStorage() {
        insertable.forEachIndexed { index, pos ->
            val slot = locatingAbsoluteSlot(pos)
            if (slot >= 0) {
                inventory[slot] = storage[index]
            }
        }
    }

    open fun insertItemStack(pos: Pos, itemStack: ItemStack): Boolean {
        val storageIndex = insertable.indexOf(pos)
        val slot = locatingAbsoluteSlot(pos)

        if (slot >= 0 && storageIndex >= 0) {
            storage[storageIndex] = itemStack
            return true
        }
        return false
    }

    open fun stackItemStack(itemStack: ItemStack): Int {
        var amount = itemStack.amount

        storage.forEachIndexed { _, stored ->
            if (stored != null && stored.isSimilar(itemStack)) {
                while (!stored.reachedMaxStackSize() && amount > 0) {
                    stored.amount++
                    amount--
                }
            }
        }

        itemStack.amount = amount

        while (amount > 0 && hasAvailableSlot()) {
            val index = storage.indexOfFirst { it.isAir }
            storage[index] = itemStack
            amount -= itemStack.amount
        }

        return amount
    }

    override fun hasAvailableSlot(): Boolean {
        return insertable.any { storage[insertable.indexOf(it)] == null }
    }

    override fun render() {
        super<ElementalPanel>.render()
        renderStorage()
    }


    override fun handleClick(pos: Pos, clickType: ClickType, e: InventoryClickEvent?): Boolean {
        require(e != null)

        val storageIndex = insertable.indexOf(pos)

        if (!super.handleClick(pos, clickType, e)) {
            // apply cursor
            e.cursor?.clone()?.let { storage[storageIndex] = it }
            // hotbar swap action
            val swapHotItem = { storage[storageIndex] = inventory.getPlayerInventory().getItem(e.hotbarButton) }
            if (e.action == InventoryAction.HOTBAR_SWAP) {
                if (e.currentItem == null) swapHotItem()
                else storage[storageIndex] = null
            } else if (e.action == InventoryAction.HOTBAR_MOVE_AND_READD) {
                swapHotItem()
            }
            // cancel event
            e.isCancelled = false
            return true
        }
        return false
    }

    override fun handleDrag(pos: List<Pos>, e: InventoryDragEvent): Boolean {
        e.newItems.entries.forEachIndexed { index, entry ->
            val itemStack = entry.value
            insertItemStack(pos[index], itemStack)
        }
        e.isCancelled = false
        return true
    }

    override fun handleItemsMove(pos: Pos, e: InventoryClickEvent): Boolean {
        val storageIndex = insertable.indexOf(pos)
        if (storageIndex < 0) return false
        e.isCancelled = false
        submit { storage[storageIndex] = inventory[locatingAbsoluteSlot(pos)] }
        return true
    }

}