package cc.trixey.invero.test

import cc.trixey.invero.api.dsl.bukkitChestWindow
import cc.trixey.invero.api.dsl.item
import cc.trixey.invero.api.dsl.pos
import cc.trixey.invero.api.dsl.standardPanel
import org.bukkit.Material
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @since 2022/12/29 13:08
 */
object UnitBasic : UnitTest {

    override fun show(player: Player) {

        bukkitChestWindow(6, "Hello Invero") {

            standardPanel(3 to 3, pos(0)) {


                item(0, Material.APPLE) {
                    modify { name = "Hello Apple" }
                }

            }

        }.open(player)

    }

}
