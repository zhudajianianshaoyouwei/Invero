package cc.trixey.invero.plugin.unit

import cc.trixey.invero.bukkit.api.dsl.*
import org.bukkit.Material
import org.bukkit.entity.Player

/**
 * Invero
 * cc.trixey.invero.plugin.unit.Freeform_Netesed
 *
 * @author Arasple
 * @since 2023/1/10 22:05
 */
fun showFreeformNetesed(player: Player) = bukkitChestWindow(6, "FreeformNetesed") {

    freeformNetesed(9 to 6) {

        standard(2 to 2, at(0)) {
            item(Material.APPLE).fillup().onClick {
                player.sendMessage("Panel 1")
            }
        }

        standard(5 to 5, at(0, 7)) {
            item(Material.EMERALD).fillup().onClick {
                player.sendMessage("Panel 2")
            }
        }

        standard(5 to 5, at(8, 7)) {
            item(Material.DIAMOND).fillup().onClick {
                player.sendMessage("Panel 3")
            }
        }

    }

    freeformNavigator()

    open(player)

}