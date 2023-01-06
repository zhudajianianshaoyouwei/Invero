package cc.trixey.invero.bukkit.nms

import cc.trixey.invero.bukkit.BukkitWindow
import org.bukkit.entity.Player
import taboolib.common.util.unsafeLazy
import taboolib.library.reflex.Reflex.Companion.setProperty
import taboolib.module.nms.nmsProxy
import taboolib.module.nms.sendPacketBlocking

/**
 * @author Arasple
 * @since 2022/10/20
 */
val handler by unsafeLazy {
    nmsProxy<NMS>()
}

fun BukkitWindow.updateTitle(title: String, updateInventory: Boolean) {
    viewers.forEach {
        val player = it.getInstance<Player>()
        val id = handler.getContainerId(player)
        handler.sendWindowOpen(player, id, type, title)
        player.updateInventory()
    }
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