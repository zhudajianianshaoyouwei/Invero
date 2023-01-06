package cc.trixey.invero.bukkit

import org.bukkit.event.inventory.*
import taboolib.common.platform.event.SubscribeEvent
import java.util.*

/**
 * @author Arasple
 * @since 2023/1/5 14:11
 */
object BukkitListener {

    @SubscribeEvent
    fun e(e: InventoryClickEvent) {
        e.inventory.holder.let {
            if (it is BukkitWindowHolder) {

//                BukkitClickEvent(e, BukkitViewer(e.whoClicked.uniqueId), it.window)
//                it.window.handleClick()
            }
        }

        e.passInventoryEvent(e.whoClicked.uniqueId)
    }

    @SubscribeEvent
    fun e(e: InventoryDragEvent) = e.passInventoryEvent(e.whoClicked.uniqueId)

    @SubscribeEvent
    fun e(e: InventoryOpenEvent) = e.passInventoryEvent(e.player.uniqueId)

    @SubscribeEvent
    fun e(e: InventoryCloseEvent) = e.passInventoryEvent(e.player.uniqueId)

    private fun InventoryEvent.passInventoryEvent(uniqueId: UUID) = inventory.holder.let {
        if (it is BukkitWindowHolder) {

            // TODO

//            it.window.handleEvent(BukkitWindowEvent(this, BukkitViewer(uniqueId), it.window))
        }
    }

}