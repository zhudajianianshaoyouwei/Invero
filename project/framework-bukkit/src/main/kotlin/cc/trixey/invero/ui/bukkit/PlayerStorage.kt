package cc.trixey.invero.ui.bukkit

import cc.trixey.invero.ui.bukkit.util.isUIMarked
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import taboolib.platform.util.isAir
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

fun Player.storePlayerInventory() {
    if (storageMap.containsKey(uniqueId)) error("Player ${name} is already stored!")

    storageMap[uniqueId] = Storage(this)
}

fun Player.restorePlayerInventory() {
    storageMap[uniqueId]?.let { backup ->
        inventory.storageContents.mapIndexed { index, itemStack ->
            if (itemStack.isNotAir()
                && !itemStack.isUIMarked()
                && backup.storage[index].isAir()
                && backup.storage.none { it?.isSimilar(itemStack) == true }
            ) {
                backup.storage[index] = itemStack
            }
        }

        inventory.storageContents = backup.storage
    }
    storageMap.remove(uniqueId)
}

class Storage(var storage: Array<ItemStack?> = arrayOfNulls(36)) {

    constructor(player: Player) : this(player.inventory.storageContents.clone())

}