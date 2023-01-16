package cc.trixey.invero.bukkit.nms

import cc.trixey.invero.bukkit.BukkitWindow
import cc.trixey.invero.bukkit.PacketWindow
import org.bukkit.entity.Player
import taboolib.common.util.unsafeLazy
import taboolib.library.reflex.Reflex.Companion.setProperty
import taboolib.module.nms.nmsProxy
import taboolib.module.nms.sendPacketBlocking

/**
 * Invero
 * cc.trixey.invero.bukkit.nms.Handler
 *
 * @author Arasple
 * @since 2022/10/20
 */
val handler by unsafeLazy {
    nmsProxy<NMS>()
}

fun BukkitWindow.updateTitle(title: String, updateInventory: Boolean = true) {
    viewers.forEach {
        val player = it.getInstance<Player>()
        val id = if (this is PacketWindow) PacketWindow.CONTAINER_ID else handler.getContainerId(player)
        handler.sendWindowOpen(player, id, type, title)

        if (updateInventory) player.updateInventory()
    }
}

fun PacketWindow.updateTitle(title: String) = updateTitle(title, false)

fun Player.sendCancelCoursor() {
    handler.sendWindowSetSlot(this, -1, -1, null, 1)
}

internal fun Player.postPacket(packet: Any, vararg fields: Pair<String, Any?>): Any {
    packet.apply {
        fields.forEach { (key, value) ->
            setProperty(key, value)
        }
        sendPacketBlocking(this)

        return this
    }
}