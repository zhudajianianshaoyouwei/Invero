package cc.trixey.invero.bukkit

import taboolib.common.platform.event.SubscribeEvent
import taboolib.module.nms.MinecraftVersion.isUniversal
import taboolib.module.nms.PacketReceiveEvent


/**
 * Invero
 * cc.trixey.invero.bukkit.PacketListener
 *
 * @author Arasple
 * @since 2022/10/20
 */
object PacketListener {

    private val indexs = if (isUniversal) {
        arrayOf("containerId", "slotNum", "buttonNum", "clickType", "carriedItem")
    } else {
        arrayOf("a", "slot", "button", "shift", "item")
    }

    @SubscribeEvent
    fun e(e: PacketReceiveEvent) {
        val player = e.player
        val packet = e.packet

        when (packet.name) {
//            "PacketPlayInWindowClick" -> {
//                val id = packet.read<Int>(indexs[0]) ?: return
//                val invero = InveroManager.findViewingInvero(player, id) ?: return
//                val slot = packet.read<Int>(indexs[1]) ?: return
//                val button = packet.read<Int>(indexs[2]) ?: return
//                val mode = Mode.valueOf(packet.read<Any>(indexs[3]).toString())
//                val type = ClickType.find(mode, button, slot) ?: return
//                val cursor = packet.read<Any?>(indexs[4])?.asCraftMirror()
//                val action = identifyingAction(player, invero, type, cursor)
//
//                if (mode == Mode.QUICK_CRAFT) {
//                    invero.handleDragEvent(type)
//                    return
//                }
//
//                invero.apply {
//                    InveroInteractEvent(player, this, type, action, slot, cursor).apply {
//                        call()
//                        interactCallback.forEach { it(this) }
//                        if (isCancelled) {
//                            player.sendCancelCoursor()
//                            refreshItem(slot)
//                        }
//                    }
//                }
//
//                e.isCancelled = false
//                player.sendActionBar("ContainerID: $id")
//            }
//
//            "PacketPlayInCloseWindow" -> {
//                val id = packet.read<Int>(if (isUniversal) "containerId" else "id") ?: return
//
//                InveroManager.findViewingInvero(player, id)?.apply {
//                    view.close()
//
//                    InveroCloseEvent(player, this).apply {
//                        call()
//                        closeCallback(this)
//                        if (shouldUpdate) {
//                            player.updateInventory()
//                        }
//                    }
//                    e.isCancelled = false
//                }
//            }
        }
    }

}