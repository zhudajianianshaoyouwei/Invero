package cc.trixey.invero.bukkit

import cc.trixey.invero.bukkit.api.findWindow
import cc.trixey.invero.bukkit.nms.persistContainerId
import cc.trixey.invero.bukkit.nms.sendCancelCoursor
import cc.trixey.invero.common.event.ClickType
import org.bukkit.event.inventory.*
import taboolib.common.platform.event.SubscribeEvent
import taboolib.module.nms.MinecraftVersion.isUniversal
import taboolib.module.nms.PacketReceiveEvent

/**
 * Invero
 * cc.trixey.invero.bukkit.Listener
 *
 * @author Arasple
 * @since 2023/1/12 18:03
 */
object Listener {

    private val FIELD_CONTAINER_ID = if (isUniversal) "containerId" else "id"
    private val FILEDS_WINDOW_CLICK =
        if (isUniversal) arrayOf("containerId", "slotNum", "buttonNum", "clickType", "carriedItem")
        else arrayOf("a", "slot", "button", "shift", "item")

    @SubscribeEvent
    fun e(e: InventoryClickEvent) = e.delegatedEvent {
        when (e.action) {
            InventoryAction.MOVE_TO_OTHER_INVENTORY -> handleItemsMove(e)
            InventoryAction.COLLECT_TO_CURSOR -> handleItemsCollect(e)
            else -> handleClick(e)
        }
    }

    @SubscribeEvent
    fun e(e: InventoryDragEvent) = e.delegatedEvent { handleDrag(e) }

    @SubscribeEvent
    fun e(e: InventoryOpenEvent) = e.delegatedEvent { handleOpen(e) }

    @SubscribeEvent
    fun e(e: InventoryCloseEvent) = e.delegatedEvent { handleClose(e) }

    private fun InventoryEvent.delegatedEvent(block: InventoryVanilla.() -> Unit) = inventory.let {
        val holder = it.holder
        if (holder is InventoryVanilla.Holder) {
            holder.getProxyInventory().block()
        }
    }

    @SubscribeEvent
    fun e(e: PacketReceiveEvent) {
        val player = e.player
        val viewer = PlayerViewer(player)
        val packet = e.packet

        when (packet.name) {
            "PacketPlayInCloseWindow" -> {
                val id = packet.read<Int>(FIELD_CONTAINER_ID) ?: return
                if (id == persistContainerId) {
                    val window = viewer.viewingWindow() ?: return
                    window.close(doCloseInventory = false, updateInventory = true)
                }
            }

            "PacketPlayInWindowClick" -> {
                packet.read<Int>(FILEDS_WINDOW_CLICK[0]).let { if (it != persistContainerId) return }
                val window = viewer.viewingWindow() ?: return
                val rawSlot = packet.read<Int>(FILEDS_WINDOW_CLICK[1]) ?: return
                val button = packet.read<Int>(FILEDS_WINDOW_CLICK[2]) ?: return
                val mode = ClickType.Mode.valueOf(packet.read<Any>(FILEDS_WINDOW_CLICK[3]).toString())
                val type = ClickType.find(mode, button, rawSlot) ?: return
                val inventory = window.inventory as InventoryPacket

                if (rawSlot >= 0) {
                    player.sendCancelCoursor()
                    inventory.update(rawSlot)
                }

                inventory.handleClickEvent(rawSlot, type)
            }
        }
    }

    private fun PlayerViewer.viewingWindow(): BukkitWindow? {
        return findWindow(name)?.let { if (it.inventory is InventoryPacket) it else null }
    }

}