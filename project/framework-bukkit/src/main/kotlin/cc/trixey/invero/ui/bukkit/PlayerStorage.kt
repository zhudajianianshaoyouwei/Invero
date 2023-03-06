package cc.trixey.invero.ui.bukkit

import cc.trixey.invero.ui.bukkit.util.isUIMarked
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import taboolib.platform.util.isNotAir
import java.util.*
import java.util.concurrent.ConcurrentHashMap

/**
 * Invero
 * cc.trixey.invero.ui.bukkit.PlayerStorage
 *
 * @author Arasple
 * @since 2023/1/20 17:16
 */
val storageMap = ConcurrentHashMap<UUID, Storage>()

fun Player.isCurrentlyStored(): Boolean {
    return storageMap.containsKey(this.uniqueId)
}

fun Player.storePlayerInventory(hidePlayerInventory: Boolean) {
    if (storageMap.containsKey(uniqueId)) error("Player $name is already stored!")

    storageMap[uniqueId] = Storage(hidePlayerInventory, this)
}

fun Player.restorePlayerInventory() = storageMap[uniqueId]?.let { backup ->

    if (!backup.isPlayerInventoryHided) {
        for (i in 0..35) {
            val currentSlot = inventory.storageContents.getOrNull(i)
            val previousSlot = backup.storage.getOrNull(i)

            // 当前背包槽位不为 UI 图标 时
            if (currentSlot?.isUIMarked() != true) {
                if (currentSlot.isNotAir()) {
                    backup.storage[i] = currentSlot
                } else if (previousSlot.isNotAir()) {
                    backup.storage[i] = null
                }
            }
        }
    }

    inventory.storageContents = backup.storage
    storageMap.remove(uniqueId)
}

class Storage(val isPlayerInventoryHided: Boolean, var storage: Array<ItemStack?> = arrayOfNulls(36)) {

    constructor(hidePlayerInventory: Boolean, player: Player) : this(
        hidePlayerInventory,
        player.inventory.storageContents.clone()
    )

}