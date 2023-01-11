package cc.trixey.invero.plugin.unit

import cc.trixey.invero.bukkit.api.dsl.*
import cc.trixey.invero.bukkit.util.randomMaterial
import org.bukkit.Material
import org.bukkit.entity.Player
import taboolib.common.platform.function.submit

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

        // viewport[8,0] = showRunningItem(pagedNetesed) test

        var itemAmount = 1

        pagedNetesed(9 to 6, at(8, 1)) {
            repeat(5) { currentPage ->
                standard(9 to 6 + 4) {
                    val material = if (currentPage == 0) Material.APPLE else randomMaterial()
                    val apple = buildItem(material) {
                        modify { name = "Running_Item" }
                        onClick { modify { amount = ++itemAmount } }
                    }

                    var position = 0
                    submit(now = false, async = true, 20L, 20L) {
                        if (noViewer()) cancel()
                        if (pageIndex == currentPage) {
                            apple.set(position, position + 1, position++ + 2)
                        }
                    }
                }
            }
        }

        nav(9 to 1, at(8, 0)) {
            pageItem(firstPaged(), +1, 0, Material.CYAN_STAINED_GLASS_PANE) { modify { name = "Preivous page" } }
            pageItem(firstPaged(), -1, 8, Material.LIME_STAINED_GLASS_PANE) { modify { name = "Next page" } }
            item(Material.GRAY_STAINED_GLASS_PANE).fillup()
        }

    }

    freeformNavigator()

    open(player)

}