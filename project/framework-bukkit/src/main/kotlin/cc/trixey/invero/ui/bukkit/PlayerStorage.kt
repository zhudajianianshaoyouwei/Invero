package cc.trixey.invero.ui.bukkit

import cc.trixey.invero.ui.common.StorageMode
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

/**
 * Invero
 * cc.trixey.invero.ui.bukkit.PlayerStorage
 *
 * @author Arasple
 * @since 2023/1/20 17:16
 */
class PlayerStorage(val player: Player, var storage: Array<ItemStack?> = arrayOfNulls(36)) {

    fun beforeOpen(storageMode: StorageMode) {
        backup(storageMode.shouldClean)
    }

    fun afterClose() {
        restore()
    }

    fun backup(clean: Boolean = false) {
        storage = player.inventory.storageContents
        if (clean) for (i in 0..35) player.inventory.storageContents[i] = null
    }

    fun restore() {
        player.inventory.storageContents = storage
    }

}