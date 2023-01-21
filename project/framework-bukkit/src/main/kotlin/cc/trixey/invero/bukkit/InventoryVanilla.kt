package cc.trixey.invero.bukkit

import cc.trixey.invero.bukkit.api.isRegistered
import cc.trixey.invero.bukkit.panel.IOStoragePanel
import cc.trixey.invero.common.event.ClickType
import org.bukkit.Bukkit
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import org.bukkit.inventory.ItemStack

/**
 * Invero
 * cc.trixey.invero.bukkit.InventoryVanilla
 *
 * @author Arasple
 * @since 2023/1/20 13:13
 */
class InventoryVanilla(override val window: BukkitWindow) : ProxyBukkitInventory {

    override fun isVirtual(): Boolean {
        return false
    }

    private val storage = PlayerStorage(viewer)

    val container = when {
        containerType.isOrdinaryChest -> Bukkit.createInventory(
            Holder(window), containerSize, inventoryTitle
        )

        else -> error("Not supported type yet")
    }

    override fun clear(slots: Collection<Int>) {
        slots.forEach { set(it, null) }
    }

    override fun get(slot: Int): ItemStack? {
        return if (slot + 1 > containerSize) {
            getPlayerInventory().getItem(slot.outflowCorrect())
        } else {
            container.getItem(slot)
        }
    }

    override fun set(slot: Int, itemStack: ItemStack?) {
        if (slot + 1 > containerSize) {
            getPlayerInventory().setItem(slot.outflowCorrect(), itemStack)
        } else {
            container.setItem(slot, itemStack)
        }
    }

    override fun isViewing(): Boolean {
        return viewer.openInventory.topInventory == container
    }

    override fun open() {
        storage.beforeOpen(window.storageMode)
        viewer.openInventory(container)
    }

    override fun close(doCloseInventory: Boolean, updateInventory: Boolean) {
        if (doCloseInventory && isViewing()) viewer.closeInventory()
        if (updateInventory) viewer.updateInventory()

        storage.afterClose(window.storageMode)
    }

    fun handleClick(e: InventoryClickEvent) {
        // 默认取消事件
        e.isCancelled = true
        // 点击的坐标
        val slot = e.rawSlot
        // 如果点击玩家背包容器
        if (slot > window.type.slotsContainer.last) {
            if (!window.storageMode.overridePlayerInventory) {
                e.isCancelled = false
                return
            }
        }
        // 转化为 x,y 定位
        val pos = window.scale.convertToPosition(slot)
        window.panels.sortedByDescending { it.weight }.forEach {
            if (pos in it.area) {
                val converted = ClickType.findBukkit(e.click.name, e.hotbarButton)
                if (it.runClickCallbacks(pos, converted, e)) {
                    it.handleClick(pos - it.locate, converted, e)
                }
                return
            }
        }
    }

    fun handleDrag(e: InventoryDragEvent) {
        // 默认取消
        e.isCancelled = true
        // 寻找 Panel 交接
        val handler = window.panels.sortedBy { it.locate }.sortedByDescending { it.weight }.find {
            e.rawSlots.all { slot -> window.scale.convertToPosition(slot) in it.area }
        }
        // 传递给 Panel 处理
        if (handler != null) {
            val affected = e.rawSlots.map { window.scale.convertToPosition(it) }
            handler.handleDrag(affected, e)
        }
    }

    fun handleItemsMove(e: InventoryClickEvent) {
        // 默认取消
        e.isCancelled = true

        val slot = e.rawSlot
        // playerInventory -> IO Panel
        if (slot > window.type.slotsContainer.last) {
            if (window.storageMode.overridePlayerInventory) return
            val insertItem = e.currentItem?.clone() ?: return

            window.getPanelsRecursively().filterIsInstance<IOStoragePanel>().sortedBy { it.locate }
                .sortedByDescending { it.weight }.forEach {
                    val previous = insertItem.amount
                    val result = it.stackItemStack(insertItem.clone())
                    insertItem.amount = result

                    if (previous != result) it.renderStorage()
                    if (result <= 0) return@forEach
                }

            e.currentItem?.amount = insertItem.amount
        }
        // IO Panel -> playerInventory
        else if (!window.storageMode.overridePlayerInventory) {
            val clickedSlot = window.scale.convertToPosition(slot)

            window.panels.sortedBy { it.locate }.sortedByDescending { it.weight }
                .find { window.scale.convertToPosition(e.rawSlot) in it.area }?.handleItemsMove(clickedSlot, e)
        }
    }

    fun handleItemsCollect(e: InventoryClickEvent) {
        // 默认取消
        e.isCancelled = true
        // 暂时未写双击收集物品的逻辑...
    }

    fun handleOpen(e: InventoryOpenEvent) {

    }

    fun handleClose(e: InventoryCloseEvent) {
        println("close window from Inventory Vanilla ${window.isRegistered()}")
        if (window.isRegistered()) {
            window.close(doCloseInventory = false, updateInventory = false)
        }
    }

    class Holder(val window: BukkitWindow) : InventoryHolder {

        override fun getInventory(): Inventory {
            return getProxyInventory().container
        }

        fun getProxyInventory(): InventoryVanilla {
            return window.inventory as InventoryVanilla
        }

    }

}