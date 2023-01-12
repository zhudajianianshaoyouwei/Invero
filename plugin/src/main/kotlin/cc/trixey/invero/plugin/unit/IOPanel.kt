package cc.trixey.invero.plugin.unit

import cc.trixey.invero.bukkit.api.dsl.bukkitChestWindow
import cc.trixey.invero.bukkit.api.dsl.item
import cc.trixey.invero.bukkit.api.dsl.set
import cc.trixey.invero.bukkit.api.dsl.storageIOPanel
import cc.trixey.invero.common.Pos
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
private val persistWindow = bukkitChestWindow(6, "IO_Storage", StorageMode(false)) {

    storageIOPanel(5 to 5, 0 to 0) {
        val border = area
            .filter { it.x == 0 || it.y == 0 || it.x == 4 || it.y == 4 }

        item(Material.CYAN_STAINED_GLASS_PANE).set(border)
        item(Material.APPLE) { modify { name = "Persist Icon Element" } }.set(Pos(1, 1))
    }

}

fun showIOStoragePanel(player: Player) = persistWindow.open(player)