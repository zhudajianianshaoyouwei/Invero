package cc.trixey.invero.plugin.unit

import cc.trixey.invero.bukkit.api.dsl.buildItem
import cc.trixey.invero.bukkit.api.dsl.bukkitChestWindow
import cc.trixey.invero.bukkit.api.dsl.scroll
import cc.trixey.invero.bukkit.util.randomMaterial
import org.bukkit.entity.Player

/**
 * Invero
 * cc.trixey.invero.plugin.unit.Scroll
 *
 * @author Arasple
 * @since 2023/1/11 14:36
 */
fun showScrollStandard(player: Player) = bukkitChestWindow(6, "Scroll_Standard") {

    scroll(9 to 6, tail = -1) {
        for (colum in 0 until 20) {
            val mat = randomMaterial()

            insertColum {
                buildItem(mat) {
                    modify { name = "ItemIndex # $it" }
                }.also { it ->
                    it.onClick {
                        player.sendMessage("$$$ $it")
                    }
                }
            }
        }
    }

    freeformNavigator()

    open(player)
}