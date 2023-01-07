package cc.trixey.invero.bukkit

import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder

/**
 * @author Arasple
 * @since 2022/12/29 12:54
 */
class BukkitWindowHolder(val window: BukkitWindow) : InventoryHolder {

    override fun getInventory(): Inventory {
        return window.inventory.container
    }

}