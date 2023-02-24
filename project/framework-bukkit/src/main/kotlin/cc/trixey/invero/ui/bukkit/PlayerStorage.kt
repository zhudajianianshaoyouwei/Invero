package cc.trixey.invero.ui.bukkit

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
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

fun Player.storePlayerInventory(wipe: Boolean = false) {
    storageMap[uniqueId] = Storage(this)
}

fun Player.restorePlayerInventory() {
    storageMap[uniqueId]?.let {
        inventory.storageContents = it.storage
    }
    storageMap.remove(uniqueId)
}

class Storage(var storage: Array<ItemStack?> = arrayOfNulls(36)) {

    constructor(player: Player) : this(player.inventory.storageContents.clone())

}