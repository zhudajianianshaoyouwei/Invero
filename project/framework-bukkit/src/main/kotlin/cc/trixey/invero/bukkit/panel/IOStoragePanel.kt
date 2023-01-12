package cc.trixey.invero.bukkit.panel

import cc.trixey.invero.bukkit.ProxyBukkitInventory
import cc.trixey.invero.bukkit.event.DelegatedClickEvent
import cc.trixey.invero.common.Pos
import cc.trixey.invero.common.Scale
import cc.trixey.invero.common.event.WindowClickEvent
import cc.trixey.invero.common.panel.ElementalPanel
import cc.trixey.invero.common.panel.IOPanel
import cc.trixey.invero.common.panel.PanelContainer
import cc.trixey.invero.common.panel.PanelWeight
import org.bukkit.inventory.ItemStack

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
) : StandardPanel(parent, weight, scale, locate), IOPanel, ElementalPanel {

    override val inventory: ProxyBukkitInventory
        get() = window.inventory as ProxyBukkitInventory

    private val storage = arrayOfNulls<ItemStack?>(scale.size)

    private val occupiedSlots by lazy {
        elements.occupiedPositions().map { it.convertToSlot(scale) }.toSet()
    }

    private val availableSlots by lazy {
        (0 until scale.size).toList() - occupiedSlots
    }

    override fun render() {
        super<ElementalPanel>.render()

        availableSlots.forEach {
            inventory[it] = storage[it]
        }
    }

    override fun handleClick(pos: Pos, e: WindowClickEvent): Boolean {
        val event = (e as DelegatedClickEvent).event
        val storageIndex = pos.convertToSlot(scale)

        if (!super.handleClick(pos, e)) {
            println("Clicked at $pos")
            storage[storageIndex] = event.currentItem
            e.isCancelled = false
            return true
        }
        return false
    }

}