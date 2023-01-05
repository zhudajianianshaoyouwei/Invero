package cc.trixey.invero.bukkit.plugin

import cc.trixey.invero.bukkit.api.dsl.bukkitChestWindow
import cc.trixey.invero.bukkit.api.dsl.fillup
import cc.trixey.invero.bukkit.api.dsl.item
import cc.trixey.invero.bukkit.api.dsl.standardPanel
import org.bukkit.Material
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @since 2022/12/29 13:08
 */
object UnitBasic {

    fun show(player: Player) {

        bukkitChestWindow(6, "Hello Invero") {

            var count = 1
            standardPanel(3 to 3) {

                item(0, Material.APPLE) {
                    modify { name = "Hello Apple" }

                    onClick {
                        modify { amount = ++count }
                        push()
                    }
                }

                item(Material.DIAMOND).fillup().onClick {
                    isCancelled = false
                }
            }

            standardPanel(3 to 7) {
                item(Material.EMERALD).fillup().onClick {
                    player.sendMessage("6")
                }
            }


        }.open(player)

    }

}
