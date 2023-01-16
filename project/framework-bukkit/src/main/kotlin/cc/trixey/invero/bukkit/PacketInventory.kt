package cc.trixey.invero.bukkit

import cc.trixey.invero.bukkit.PacketWindow.Companion.PACKET_WINDOW_ID
import cc.trixey.invero.bukkit.nms.handler
import cc.trixey.invero.bukkit.util.safeBukkitPlayer
import cc.trixey.invero.common.Viewer
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.PlayerInventory

/**
 * Invero
 * cc.trixey.invero.bukkit.PacketInventory
 *
 * @author Arasple
 * @since 2023/1/12 16:03
 */
class PacketInventory(override val window: PacketWindow) : ProxyBukkitInventory {

    private val playerItems = mutableMapOf<Viewer, PlayerItems>()
    private val windowItems = arrayOfNulls<ItemStack?>(window.type.entireWindowSize)

    fun updateWindowItems(viewer: Viewer) {
        handler.sendWindowItems(viewer.getInstance(), PACKET_WINDOW_ID, windowItems.toList())
    }

    fun updateWindowSlot(viewer: Viewer, slot: Int) {
        handler.sendWindowSetSlot(viewer.getInstance(), PACKET_WINDOW_ID, slot, windowItems[slot])
    }

    override fun get(slot: Int): ItemStack? {
        return windowItems[slot]
    }

    override fun set(slot: Int, itemStack: ItemStack?) {
        windowItems[slot] = itemStack
        window.forViewers<BukkitViewer> { updateWindowSlot(it, slot) }
    }

    override fun open(viewer: Viewer) {
        if (storageMode.shouldBackup) {
            getPlayerInventory(viewer).apply {
                playerItems[viewer] = PlayerItems(storageContents)

                if (storageMode.shouldClean) clear()
            }
        }

        handler.sendWindowOpen(viewer.getInstance(), PACKET_WINDOW_ID, window.type, window.title)
    }

    override fun close(viewer: Viewer, updateInventory: Boolean) {
        playerItems.remove(viewer)?.let {
            if (storageMode.shouldRestore) {
                getPlayerInventory(viewer).apply {
                    storageContents = it.storage
                }
            }
        }

        if (updateInventory) {
            handler.sendWindowClose(viewer.getInstance(), PACKET_WINDOW_ID)
            viewer.safeBukkitPlayer()?.updateInventory()
        }
    }

    override fun closeAll() {
        window.forViewers<Viewer> {
            close(it, true)
        }
    }

    override fun getContainerSize(): Int {
        return window.type.containerSize
    }

    override fun clear() {
        windowItems.indices.forEach { windowItems[it] = null }
        window.forViewers<BukkitViewer> { updateWindowItems(it) }
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

}