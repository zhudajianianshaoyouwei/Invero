package cc.trixey.invero.plugin.demo

import cc.trixey.invero.bukkit.api.dsl.*
import cc.trixey.invero.bukkit.util.randomMaterial
import org.bukkit.Material
import org.bukkit.entity.Player
import taboolib.common.platform.function.submit

/**
 * Invero
 * cc.trixey.invero.plugin.demo.Basics
 *
 * @author Arasple
 * @since 2022/12/29 13:08
 */
fun showBasic(player: Player) = chestWindow(player, 6, "Hello InveroPlugin") {

    var count = 1

    standard(3 to 3) {
        item(0, Material.APPLE) {
            modify { name = "Hello Apple" }
            click { modify { amount = ++count } }
        }

        buildItem(Material.DIAMOND).fillup()
    }

    pagedNetesed(3 to 7) {

        repeat(10) { page ->

            group {

                standard(3 to 6) {
                    buildItem(randomMaterial()).fillup().click {
                        player.sendMessage("Page $page")
                    }
                }
                standard(3 to 1) {
                    buildItem(randomMaterial()).fillup().click {
                        player.sendMessage("BAR")
                    }
                }

            }

        }

    }

    nav(3 to 1) {
        pageController(firstPagedPanel(), -1, 0, Material.CYAN_STAINED_GLASS_PANE) { modify { name = "Preivous page" } }
        pageController(firstPagedPanel(), +1, 2, Material.LIME_STAINED_GLASS_PANE) { modify { name = "Next page" } }
    }
    open()
}

fun showRunningItem(player: Player) = chestWindow(player, 6, "Running Paged Item") {

    var itemAmount = 1

    pagedNetesed(9 to 10 - 1, at(y = 1)) {
        repeat(5) { currentPage ->
            standard {
                val material = if (currentPage == 0) Material.APPLE else randomMaterial()
                val apple = buildItem(material) {
                    modify { name = "Running_Item" }
                    click { modify { amount = ++itemAmount } }
                }

                var position = 0
                submit(now = false, async = true, 20L, 20L) {
                    if (!isViewing()) cancel()
                    if (pageIndex == currentPage) {
                        apple.set(position, position + 1, position++ + 2)
                    }
                }
            }
        }
    }

    nav(9 to 1, at(x = 0)) {
        pageController(firstPagedPanel(), -1, 0, Material.CYAN_STAINED_GLASS_PANE) { modify { name = "Preivous page" } }
        pageController(firstPagedPanel(), +1, 8, Material.LIME_STAINED_GLASS_PANE) { modify { name = "Next page" } }
        item(Material.GRAY_STAINED_GLASS_PANE).fillup()
    }

    open()
}

fun showDynamicTitle(player: Player) = chestWindow(player, 3, "_") {

    standard(9 to 3) {
        getUnoccupiedPositions().let { posSet ->
            posSet.forEach { pos ->
                buildItem(randomMaterial()) {
                    onClick { type, _ ->
                        modify { amount += (if (type.isLeftClick) 1 else -1) }
                        player.sendMessage(pos.toString())
                    }
                }.add(pos)
            }
        }
    }

    open()

}.also {

    val dynamicTitles = run {
        var current = "_"
        val titles = mutableListOf<String>()

        "Invero Animated MenuTitle".windowed(1, 1).forEachIndexed { _, s ->
            current += s
            titles.add(current)
        }
        titles.addAll(titles.reversed())
        titles
    }

}