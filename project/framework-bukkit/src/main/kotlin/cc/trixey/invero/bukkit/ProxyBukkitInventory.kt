package cc.trixey.invero.bukkit

import cc.trixey.invero.common.ProxyInventory
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.PlayerInventory

/**
 * Invero
 * cc.trixey.invero.bukkit.ProxyBukkitInventory
 *
 * @author Arasple
 * @since 2023/1/20 13:15
 */
interface ProxyBukkitInventory : ProxyInventory {

    val viewer: Player
        get() = window.viewer.get()

    fun getPlayerInventory(): PlayerInventory {
        return viewer.inventory
    }

    operator fun get(slot: Int): ItemStack?

    operator fun set(slot: Int, itemStack: ItemStack?)

    fun Int.outflowCorrect() = (this - containerSize).let {
        if (it > 26) it - 27 else it + 9
    }

}