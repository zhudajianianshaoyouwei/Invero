package cc.trixey.invero.bukkit

import cc.trixey.invero.common.ProxyInventory
import cc.trixey.invero.common.Viewer
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.PlayerInventory

/**
 * @author Arasple
 * @since 2022/12/30 13:11
 */
interface ProxyBukkitInventory : ProxyInventory {

    fun getPlayerInventory(viewer: Viewer): PlayerInventory

    fun getPlayerInventorySafely(viewer: Viewer): PlayerInventory?

    operator fun get(slot: Int): ItemStack?

    operator fun set(slot: Int, itemStack: ItemStack?)

    fun Int.outflowCorrect() = (this - getContainerSize()).let {
        if (it > 26) it - 27 else it + 9
    }

}