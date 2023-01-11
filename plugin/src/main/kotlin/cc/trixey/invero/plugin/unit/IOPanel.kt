package cc.trixey.invero.plugin.unit

import cc.trixey.invero.bukkit.api.dsl.bukkitChestWindow
import cc.trixey.invero.bukkit.api.dsl.item
import cc.trixey.invero.bukkit.api.dsl.set
import cc.trixey.invero.bukkit.api.dsl.storageIOPanel
import cc.trixey.invero.common.StorageMode
import org.bukkit.Material
import org.bukkit.entity.Player

/**
 * Invero
 * cc.trixey.invero.plugin.unit.IOPanel
 *
 * @author Arasple
 * @since 2023/1/11 17:50
 */
fun showIOStoragePanel(player: Player) = bukkitChestWindow(6, "IO_Storage", StorageMode(false)) {

    storageIOPanel(9 to 6) {
        item(Material.APPLE).set(0, 1, 2, 3, 4, 5, 6, 7, 8)
    }

    open(player)
}