package cc.trixey.invero.plugin.demo

import cc.trixey.invero.ui.bukkit.api.dsl.*
import cc.trixey.invero.ui.bukkit.util.randomMaterial
import cc.trixey.invero.ui.common.scroll.ScrollDirection
import org.bukkit.Material
import org.bukkit.entity.Player

/**
 * Invero
 * cc.trixey.invero.plugin.demo.Scroll
 *
 * @author Arasple
 * @since 2023/1/11 14:36
 */

fun showScroll2(player: Player) = chestWindow(player, 6, "Loop Scroll (horizontally)") {

    scroll(9 to 5, direction = ScrollDirection.HORIZONTAL, tail = -1) {

        repeat(10) {
            val columTexture = randomMaterial()

            insertColum {
                buildItem(columTexture) { modify { name = "#$it" } }
            }
        }

    }

    nav(9 to 1) {
        val target = firstScrollPanel()

        scrollController(target, -1, 0, Material.CYAN_STAINED_GLASS_PANE) {
            modify { name = "§3← LEFT" }
        }
        scrollController(target, +1, 8, Material.LIME_STAINED_GLASS_PANE) {
            modify { name = "§a→ RIGHT" }
        }

        item(Material.GRAY_STAINED_GLASS_PANE).fillup()
    }

    open()
}

fun showScrollStandard(player: Player) = chestWindow(player, 6, "Scroll_Standard") {

    scroll(9 to 6, tail = -1) {
        for (colum in 0 until 20) {
            val mat = randomMaterial()

            insertColum { buildItem(mat) }
        }
    }

    freeformNavigator()

    open()
}