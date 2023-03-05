package cc.trixey.invero.plugin.demo

import cc.trixey.invero.ui.bukkit.WindowChest
import cc.trixey.invero.ui.bukkit.api.dsl.*
import cc.trixey.invero.ui.common.Pos
import org.bukkit.Material
import org.bukkit.entity.Player

/**
 * Invero
 * cc.trixey.invero.plugin.demo.IOPanel
 *
 * @author Arasple
 * @since 2023/1/11 17:50
 */
private var persistWindow: WindowChest? = null

fun showIOStoragePanel(player: Player) {
    if (persistWindow == null) {
        persistWindow = chestWindow(player.viewer, 6, "IO_Storage", false, virtual = false) {
            craftingIOPanel(5 to 5, 0 to 0) {
                val border = area
                    .filter { it.x == 0 || it.y == 0 || it.x == 4 || it.y == 4 }

                item(Material.CYAN_STAINED_GLASS_PANE).set(border)
                item(Material.APPLE) { modify { name = "Persist Icon Element" } }.set(Pos(1, 1))
            }
        }
    }
    persistWindow!!.open()
}
