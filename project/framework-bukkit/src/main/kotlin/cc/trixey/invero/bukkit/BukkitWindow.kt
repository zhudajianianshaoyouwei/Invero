package cc.trixey.invero.bukkit

import cc.trixey.invero.bukkit.api.InveroAPI
import cc.trixey.invero.bukkit.event.DelegatedDragEvent
import cc.trixey.invero.bukkit.event.DelegatedItemsCollectEvent
import cc.trixey.invero.bukkit.event.DelegatedItemsMoveEvent
import cc.trixey.invero.bukkit.nms.updateTitle
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

    abstract override val inventory: BukkitInventory

    override var title = title
        set(value) {
            field = value
            submit(async = true) {
                updateTitle(value, true)
            }
        }

    override val viewers = mutableSetOf<Viewer>()

    override val panels = arrayListOf<Panel>()

    override val size = type.entireWindowSize

    fun open(player: Player) = open(BukkitViewer(player))

    override fun render() {
        panels
            .sortedBy { it.weight }
            .forEach {
                println("[WINDOW RENDER PANEL] ${it.javaClass.simpleName} (${it.scale}) at ${it.locate}")
                it.render()
            }
    }

    override fun open(viewer: Viewer) {
        if (viewers.add(viewer)) {
            if (viewers.size == 1) InveroAPI.manager.register(this)
            inventory.open(viewer)
            render()
        } else {
            error("Viewer {$viewer} is already viewing this window")
        }
    }

    override fun close(viewer: Viewer) {
        if (viewers.remove(viewer)) inventory.close(viewer)
        if (viewers.isEmpty()) {
            InveroAPI.manager.unregister(this)
        }
    }

    override fun handleOpen(e: WindowOpenEvent) {

    }

    override fun handleClose(e: WindowCloseEvent) {
        close(e.viewer)
    }

    override fun handleClick(e: WindowClickEvent) {
        e.isCancelled = true

        val window = e.window as BukkitWindow
        val rawSlot = e.rawSlot

        if (rawSlot > window.type.slotsContainer.last) {
            if (!window.storageMode.overridePlayerInventory) {
                e.isCancelled = false
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

    override fun handleItemsCollect(e: WindowItemsCollectEvent) {
        val event = (e as DelegatedItemsCollectEvent).event

        println("================================> WindowItemsCollectEvent")
        println("      action: " + event.action)
        println("       click: " + event.click)
        println(" currentItem: " + event.currentItem)
        println("      cursor: " + event.cursor)
        println("hotbarButton: " + event.hotbarButton)
        println("     rawSlot: " + event.rawSlot)
        println("        slot: " + event.slot)
        println("    slotType: " + event.slotType)
    }

    override fun handleItemsMove(e: WindowItemsMoveEvent) {
        e.isCancelled = true

        val event = (e as DelegatedItemsMoveEvent).event

        println("================================> WindowItemsMoveEvent")
        println("      action: " + event.action)
        println("       click: " + event.click)
        println(" currentItem: " + event.currentItem)
        println("      cursor: " + event.cursor)
        println("hotbarButton: " + event.hotbarButton)
        println("     rawSlot: " + event.rawSlot)
        println("        slot: " + event.slot)
        println("    slotType: " + event.slotType)
    }

}