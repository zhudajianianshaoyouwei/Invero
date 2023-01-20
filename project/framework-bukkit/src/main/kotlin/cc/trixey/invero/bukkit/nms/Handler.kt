package cc.trixey.invero.bukkit.nms

import cc.trixey.invero.bukkit.BukkitWindow
import cc.trixey.invero.bukkit.InventoryPacket
import cc.trixey.invero.bukkit.api.findWindow
import org.bukkit.entity.Player
import taboolib.common.platform.function.submitAsync
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
const val persistContainerId: Int = 119

val handler by unsafeLazy {
    nmsProxy<NMS>()
}

fun BukkitWindow.updateTitle(title: String, updateInventory: Boolean = true) {
    val player = viewer.get<Player>()
    val virtual = inventory is InventoryPacket
    val id = if (virtual) persistContainerId else handler.getContainerId(player)

    handler.sendWindowOpen(player, id, type, title)
    if (updateInventory) player.updateInventory()
    if (virtual) (inventory as InventoryPacket).update()

    submitAsync { if (findWindow(viewer.name) == null) handler.sendWindowClose(player, id) }
}

fun Player.sendCancelCoursor() {
    handler.sendWindowSetSlot(this, -1, -1, null, 1)
}

internal fun Player.postPacket(packet: Any, vararg fields: Pair<String, Any?>) = packet.apply {
    fields.forEach { (key, value) -> setProperty(key, value) }
    sendPacketBlocking(this)
    return this
}