package cc.trixey.invero.bukkit.panel

import cc.trixey.invero.bukkit.ProxyBukkitInventory
import cc.trixey.invero.bukkit.event.DelegatedClickEvent
import cc.trixey.invero.bukkit.event.DelegatedDragEvent
import cc.trixey.invero.common.Pos
import cc.trixey.invero.common.Scale
import cc.trixey.invero.common.event.WindowClickEvent
import cc.trixey.invero.common.event.WindowDragEvent
import cc.trixey.invero.common.panel.ElementalPanel
import cc.trixey.invero.common.panel.IOPanel
import cc.trixey.invero.common.panel.PanelContainer
import cc.trixey.invero.common.panel.PanelWeight
import cc.trixey.invero.common.util.locatingAbsoluteSlot
import org.bukkit.inventory.ItemStack

/**
 * Invero
 * cc.trixey.invero.bukkit.panel.IOStoragePanel
 *
 * @author Arasple
 * @since 2023/1/11 17:47
 */
open class IOStoragePanel(
    parent: PanelContainer,
    weight: PanelWeight,
    scale: Scale,
    locate: Pos
) : StandardPanel(parent, weight, scale, locate), IOPanel, ElementalPanel {

    override val inventory: ProxyBukkitInventory
        get() = window.inventory as ProxyBukkitInventory

    private val storage = arrayOfNulls<ItemStack?>(scale.size)

    override val positionsInsertable: List<Pos> by lazy { (area - positionsLocked.toSet()).sorted() }

    override val positionsLocked: List<Pos> by lazy { elements.occupiedPositions().sorted() }

    override fun renderStorage() {
        positionsInsertable.forEachIndexed { index, pos ->
            val slot = locatingAbsoluteSlot(pos, this)
            if (slot >= 0) {
                inventory[slot] = storage[index]
            }
        }
    }

    private fun insertItemStack(pos: Pos, itemStack: ItemStack) {
        val storageIndex = positionsInsertable.indexOf(pos)
        val slot = locatingAbsoluteSlot(pos, this)

        if (slot >= 0 && storageIndex >= 0) {
            storage[storageIndex] = itemStack
        } else {
            error("insertItemStack: $pos / $itemStack // $slot")
        }
    }

    override fun render() {
        super<ElementalPanel>.render()
        renderStorage()
    }

    override fun handleClick(pos: Pos, e: WindowClickEvent): Boolean {
        val event = (e as DelegatedClickEvent).event
        val storageIndex = positionsInsertable.indexOf(pos)

        if (!super.handleClick(pos, e)) {
            storage[storageIndex] = event.cursor?.clone()

            e.isCancelled = false
            return true
        }
        return false
    }

    override fun handleDrag(positions: List<Pos>, e: WindowDragEvent): Boolean {
        val event = (e as DelegatedDragEvent).event

        event.newItems.entries.forEachIndexed { index, entry ->
            val itemStack = entry.value
            insertItemStack(positions[index], itemStack)
        }

        e.isCancelled = false
        return true
    }

}