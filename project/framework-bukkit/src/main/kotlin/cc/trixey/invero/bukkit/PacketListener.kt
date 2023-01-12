package cc.trixey.invero.bukkit

import cc.trixey.invero.bukkit.PacketWindow.Companion.CONTAINER_ID
import cc.trixey.invero.bukkit.api.InveroAPI
import cc.trixey.invero.bukkit.event.PacketWindowClickEvent
import cc.trixey.invero.bukkit.event.PacketWindowCloseEvent
import cc.trixey.invero.bukkit.nms.senddCancelCoursor
import cc.trixey.invero.common.event.ClickType
import taboolib.common.platform.event.SubscribeEvent
import taboolib.module.nms.MinecraftVersion.isUniversal
import taboolib.module.nms.PacketReceiveEvent

/**
 * Invero
 * cc.trixey.invero.bukkit.PacketHandler
 *
 * @author Arasple
 * @since 2023/1/12 18:03
 */
object PacketListener {

    private val FIELD_CONTAINER_ID = if (isUniversal) "containerId" else "id"
    private val FILEDS_WINDOW_CLICK =
        if (isUniversal) arrayOf("containerId", "slotNum", "buttonNum", "clickType", "carriedItem")
        else arrayOf("a", "slot", "button", "shift", "item")

    @SubscribeEvent
    fun e(e: PacketReceiveEvent) {
        val player = e.player
        val viewer = BukkitViewer(player)
        val packet = e.packet

        when (packet.name) {
            "PacketPlayInCloseWindow" -> {
                val id = packet.read<Int>(FIELD_CONTAINER_ID) ?: return
                if (id == CONTAINER_ID) {
                    val window = viewer.viewingPacketWindow() ?: return

                    PacketWindowCloseEvent(viewer, window).call()
                    window.close(viewer)

                    e.isCancelled = false
                }
            }

            "PacketPlayInWindowClick" -> {
                packet.read<Int>(FILEDS_WINDOW_CLICK[0]).let { if (it != CONTAINER_ID) return }
                val window = viewer.viewingPacketWindow() ?: return
                val rawSlot = packet.read<Int>(FILEDS_WINDOW_CLICK[1]) ?: return
                val button = packet.read<Int>(FILEDS_WINDOW_CLICK[2]) ?: return
                val mode = ClickType.Mode.valueOf(packet.read<Any>(FILEDS_WINDOW_CLICK[3]).toString())
                val type = ClickType.find(mode, button, rawSlot) ?: return

                if (rawSlot >= 0) {
                    player.senddCancelCoursor()
                    window.inventory.updateWindowSlot(viewer, rawSlot)
                }

                PacketWindowClickEvent(viewer, window, rawSlot, type).call()
                e.isCancelled = false
            }
        }
    }

    private fun BukkitViewer.viewingPacketWindow(): PacketWindow? {
        return InveroAPI.findWindow(this).let { if (it !is PacketWindow) null else it }
    }

}

