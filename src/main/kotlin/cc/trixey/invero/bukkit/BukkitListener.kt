package cc.trixey.invero.bukkit

import cc.trixey.invero.common.event.BukkitWindowEvent
import org.bukkit.event.inventory.*
import taboolib.common.platform.event.SubscribeEvent
import java.util.*

/**
 * @author Arasple
 * @since 2023/1/5 14:11
 */
object BukkitListener {

    @SubscribeEvent
    fun e(e: InventoryClickEvent) = e.passInventoryEvent(e.whoClicked.uniqueId)

    @SubscribeEvent
    fun e(e: InventoryDragEvent) = e.passInventoryEvent(e.whoClicked.uniqueId)

    @SubscribeEvent
    fun e(e: InventoryOpenEvent) = e.passInventoryEvent(e.player.uniqueId)

    @SubscribeEvent
    fun e(e: InventoryCloseEvent) = e.passInventoryEvent(e.player.uniqueId)

    private fun InventoryEvent.passInventoryEvent(uniqueId: UUID) = inventory.holder.let {
        if (it is BukkitWindowHolder) {
            it.window.handleEvent(BukkitWindowEvent(this, BukkitViewer(uniqueId), it.window))
        }
    }

}