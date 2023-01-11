package cc.trixey.invero.plugin.unit

import cc.trixey.invero.bukkit.api.dsl.bukkitChestWindow
import cc.trixey.invero.bukkit.api.dsl.storageIOPanel
import org.bukkit.entity.Player

/**
 * Invero
 * cc.trixey.invero.plugin.unit.IOPanel
 *
 * @author Arasple
 * @since 2023/1/11 17:50
 */
fun showIOStoragePanel(player: Player) = bukkitChestWindow(6, "IO_Storage") {

    storageIOPanel(9 to 6) {

    }

    open(player)
}