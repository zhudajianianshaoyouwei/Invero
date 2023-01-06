package cc.trixey.invero.bukkit

import cc.trixey.invero.bukkit.event.*
import cc.trixey.invero.common.Window
import org.bukkit.event.inventory.*
import taboolib.common.platform.event.SubscribeEvent

/**
 * @author Arasple
 * @since 2023/1/5 14:11
 */
object BukkitListener {

    @SubscribeEvent
    fun e(e: InventoryClickEvent) = e.delegatedEvent {
        // def cancel
        e.isCancelled = true

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
    fun e(e: InventoryCloseEvent) = e.delegatedEvent { handleClose(DelegatedCloseEvent(e)) }

    private fun InventoryEvent.delegatedEvent(block: Window.() -> Unit) = inventory.holder.let {
        if (it is BukkitWindowHolder) {
            it.window.block()
        }
    }

}