package cc.trixey.invero.bukkit

import cc.trixey.invero.bukkit.api.InveroAPI
import cc.trixey.invero.bukkit.event.*
import org.bukkit.entity.Player
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.inventory.*
import org.bukkit.event.player.PlayerChangedWorldEvent
import org.bukkit.event.player.PlayerQuitEvent
import taboolib.common.platform.event.SubscribeEvent

/**
 * Invero
 * cc.trixey.invero.bukkit.BukkitListener
 *
 * @author Arasple
 * @since 2023/1/5 14:11
 */
object BukkitListener {

    /*
    Player safety
     */

    @SubscribeEvent
    fun e(e: PlayerDeathEvent) = e.entity.windowClosure()

    @SubscribeEvent
    fun e(e: PlayerChangedWorldEvent) = e.player.windowClosure()

    @SubscribeEvent
    fun e(e: PlayerQuitEvent) = e.player.windowClosure()

    /*
    Bukkit.InventoryEvent
     */

    @SubscribeEvent
    fun e(e: InventoryClickEvent) = e.delegatedEvent {
        when (e.action) {
            InventoryAction.MOVE_TO_OTHER_INVENTORY -> handleItemsMove(DelegatedItemsMoveEvent(e))
            InventoryAction.COLLECT_TO_CURSOR -> handleItemsCollect(DelegatedItemsCollectEvent(e))
            else -> handleClick(DelegatedClickEvent(e))
        }
    }

    @SubscribeEvent
    fun e(e: InventoryDragEvent) = e.delegatedEvent { handleDrag(DelegatedDragEvent(e)) }

    @SubscribeEvent
    fun e(e: InventoryOpenEvent) = e.delegatedEvent { handleOpen(DelegatedOpenEvent(e)) }

    @SubscribeEvent
    fun e(e: InventoryCloseEvent) = e.delegatedEvent {
        close(viewer = BukkitViewer(e.player.uniqueId))
        handleClose(DelegatedCloseEvent(e))
    }

    /*
    PacketWindow.Events
     */

    @SubscribeEvent
    fun e(e: PacketWindowOpenEvent) = e.delegatedEvent { handleOpen(e) }

    @SubscribeEvent
    fun e(e: PacketWindowCloseEvent) = e.delegatedEvent { handleClose(e) }

    @SubscribeEvent
    fun e(e: PacketWindowClickEvent) = e.delegatedEvent { handleClick(e) }

    /*
    Private function helper
     */

    private fun InventoryEvent.delegatedEvent(block: BukkitWindow.() -> Unit) = inventory.holder.let {
        if (it is BukkitWindowHolder) {
            it.window.block()
        }
    }

    private fun PacketWindowEvent.delegatedEvent(block: PacketWindow.() -> Unit) = block(window as PacketWindow)

    private fun Player.windowClosure() {
        val viewer = BukkitViewer(this)
        InveroAPI.findWindow(viewer)?.close(viewer)
    }

}