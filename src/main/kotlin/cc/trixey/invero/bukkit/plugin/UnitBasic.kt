package cc.trixey.invero.bukkit.plugin

import cc.trixey.invero.bukkit.api.bukkitChestWindow
import cc.trixey.invero.bukkit.api.item
import cc.trixey.invero.bukkit.api.standardPanel
import org.bukkit.Material
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @since 2022/12/29 13:08
 */
object UnitBasic {

    fun show(player: Player) {

        bukkitChestWindow(6, "Hello Invero") {

            standardPanel(3 to 3, 0 to 0) {


                item(0, Material.APPLE) {
                    modify { name = "Hello Apple" }
                }

            }


        }.open(player)

    }

}
