package cc.trixey.invero.bukkit

import cc.trixey.invero.bukkit.api.InveroAPI
import cc.trixey.invero.bukkit.event.DelegatedDragEvent
import cc.trixey.invero.bukkit.event.DelegatedItemsMoveEvent
import cc.trixey.invero.bukkit.nms.updateTitle
import cc.trixey.invero.bukkit.panel.IOStoragePanel
import cc.trixey.invero.common.*
import cc.trixey.invero.common.event.*
import org.bukkit.entity.Player
import taboolib.common.platform.function.submit

/**
 * Invero
 * cc.trixey.invero.bukkit.BukkitWindow
 *
 * @author Arasple
 * @since 2022/12/29 12:54
 */
abstract class BukkitWindow(
    val type: WindowType,
    override val storageMode: StorageMode = StorageMode(overridePlayerInventory = true, alwaysClean = true),
    title: String = "Untitled_Invero_Window"
) : Window {

    abstract override val inventory: ProxyBukkitInventory

    override var title = title
        set(value) {
            field = value
            submit { updateTitle(value, true) }
        }

    override val viewers = arrayListOf<Viewer>()

    override val panels = arrayListOf<Panel>()

    override val size = type.entireWindowSize

    fun priorityViewer(): BukkitViewer? {
        return viewers.firstOrNull() as BukkitViewer?
    }

    fun open(player: Player) = open(BukkitViewer(player))

    override fun render() {
        panels
            .sortedBy { it.weight }
            .forEach { it.render() }
    }

    override fun open(viewer: Viewer) {
        if (viewers.add(viewer)) {
            if (viewers.size == 1) InveroAPI.bukkitManager.register(this)
            inventory.open(viewer)
            render()
        } else {
            error("Viewer {$viewer} is already viewing this window")
        }
    }

    override fun close(viewer: Viewer) {
        if (viewers.remove(viewer)) inventory.close(viewer)
        if (viewers.isEmpty()) {
            InveroAPI.bukkitManager.unregister(this)
        }
    }

    override fun handleOpen(e: WindowOpenEvent) {}

    override fun handleClose(e: WindowCloseEvent) {}

    override fun handleClick(e: WindowClickEvent) {
        e.clickCancelled = true

        val window = e.window as BukkitWindow
        val rawSlot = e.rawSlot

        // click player inventory
        if (rawSlot > window.type.slotsContainer.last) {
            if (!window.storageMode.overridePlayerInventory) {
                e.clickCancelled = false
                return
            }
        }

        val clickedSlot = scale.convertToPosition(rawSlot)

        panels
            .sortedByDescending { it.weight }
            .forEach {
                if (clickedSlot in it.area) {
                    if ((it as BukkitPanel).runHandler(e)) {
                        it.handleClick(clickedSlot - it.locate, e)
                    }
                    return
                }
            }
    }

    override fun handleDrag(e: WindowDragEvent) {
        e.isCancelled = true

        val event = (e as DelegatedDragEvent).event
        val handler = panels
            .sortedBy { it.locate }
            .sortedByDescending { it.weight }
            .find {
                event.rawSlots.all { slot -> scale.convertToPosition(slot) in it.area }
            }

        if (handler != null) {
            val affected = event.rawSlots.map { scale.convertToPosition(it) }
            handler.handleDrag(affected, e)
        }
    }

    override fun handleItemsMove(e: WindowItemsMoveEvent) {
        e.isCancelled = true

        val event = (e as DelegatedItemsMoveEvent).event
        val window = e.window as BukkitWindow
        val rawSlot = event.rawSlot

        // playerInventory -> IO Panel
        if (rawSlot > window.type.slotsContainer.last) {
            if (window.storageMode.overridePlayerInventory) return
            val insertItem = event.currentItem?.clone() ?: return

            getPanelsRecursively()
                .filterIsInstance<IOStoragePanel>()
                .sortedBy { it.locate }
                .sortedByDescending { it.weight }
                .forEach {
                    val previous = insertItem.amount
                    val result = it.stackItemStack(insertItem.clone())
                    insertItem.amount = result

                    if (previous != result) it.renderStorage()
                    if (result <= 0) return@forEach
                }

            event.currentItem?.amount = insertItem.amount
        }
        // IO Panel -> playerInventory
        else if (!window.storageMode.overridePlayerInventory) {
            val clickedSlot = scale.convertToPosition(rawSlot)

            panels
                .sortedBy { it.locate }
                .sortedByDescending { it.weight }
                .find { scale.convertToPosition(event.rawSlot) in it.area }
                ?.handleItemsMove(clickedSlot, e)
        }
    }

    // TODO 懒得做了
    override fun handleItemsCollect(e: WindowItemsCollectEvent) {
        e.isCancelled = true
    }

}