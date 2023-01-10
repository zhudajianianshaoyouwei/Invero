package cc.trixey.invero.bukkit

import cc.trixey.invero.bukkit.util.safeBukkitPlayer
import cc.trixey.invero.common.Viewer
import cc.trixey.invero.common.Window
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.PlayerInventory

/**
 * Invero
 * cc.trixey.invero.bukkit.BukkitInventory
 *
 * @author Arasple
 * @since 2022/12/30 12:57
 */
class BukkitInventory(
    private val window: Window,
    val container: Inventory,
    var emptyPlayerInventory: Boolean = true
) : ProxyBukkitInventory {

    private val playerItems = mutableMapOf<Viewer, PlayerItems>()

    override fun getWindow(): Window {
        return window
    }

    override fun close(viewer: Viewer, updateInventory: Boolean) {
        viewer.safeBukkitPlayer()?.let {
            it.closeInventory()
            it.updateInventory()

            playerItems.remove(viewer)?.let {
                getPlayerInventory(viewer).apply {
                    storageContents = it.storage
                }
            }
        }
    }

    override fun open(viewer: Viewer) {
        getPlayerInventory(viewer).apply {
            playerItems[viewer] = PlayerItems(storageContents)
            if (emptyPlayerInventory) clear()
        }
        viewer.safeBukkitPlayer()?.openInventory(container)
    }

    override fun closeAll() {
        window.forViewers<Viewer> {
            close(it, true)
        }
    }

    override fun getContainerSize(): Int {
        return container.size
    }

    override fun clear() {
        container.clear()
    }

    override fun clear(slots: Collection<Int>) {
        slots.forEach { set(it, null) }
    }

    override fun getPlayerInventory(viewer: Viewer): PlayerInventory {
        return getPlayerInventorySafely(viewer)!!
    }

    override fun getPlayerInventorySafely(viewer: Viewer): PlayerInventory? {
        return viewer.safeBukkitPlayer()?.inventory
    }

    override operator fun get(slot: Int): ItemStack? {
        return if (slot + 1 > getContainerSize()) {
            getPlayerInventory(window.viewers.first()).getItem(slot.outflowCorrect())
        } else {
            container.getItem(slot)
        }
    }

    override operator fun set(slot: Int, itemStack: ItemStack?) {
        if (slot + 1 > getContainerSize()) {
            window.viewers.forEach {
                getPlayerInventory(it).setItem(slot.outflowCorrect(), itemStack)
            }
        } else {
            container.setItem(slot, itemStack)
        }
    }

}