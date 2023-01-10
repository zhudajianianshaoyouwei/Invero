package cc.trixey.invero.plugin.unit

import cc.trixey.invero.bukkit.api.dsl.*
import cc.trixey.invero.bukkit.util.launchAsync
import cc.trixey.invero.bukkit.util.randomMaterial
import org.bukkit.Material
import org.bukkit.entity.Player
import taboolib.common.platform.function.submit

/**
 * Invero
 * cc.trixey.invero.plugin.unit.Basics
 *
 * @author Arasple
 * @since 2022/12/29 13:08
 */
fun showBasic(player: Player) = bukkitChestWindow(6, "Hello InveroPlugin") {

    var count = 1

    standard(3 to 3) {

        item(0, Material.APPLE) {
            modify { name = "Hello Apple" }
            onClick { modify { amount = ++count } }
        }

        buildItem(Material.DIAMOND).fillup()

    }

    pagedNetesed(3 to 7) {

        for (i in 0..10) {
            standard {
                buildItem(randomMaterial()).fillup().onClick {
                    player.sendMessage("Page $i")
                }
            }
        }

    }

    nav(3 to 1) {
        item(0, Material.CYAN_STAINED_GLASS_PANE) {
            modify { name = "Preivous page" }
            onClick { firstPagedNetesed().previousPage() }
        }
        item(2, Material.LIME_STAINED_GLASS_PANE) {
            modify { name = "Next page" }
            onClick { firstPagedNetesed().nextPage() }
        }
    }


}.open(player)

fun showRunningApple(player: Player) = bukkitChestWindow(6, "Running apple") {

    var count = 1

    standard(9 to 6 + 4) {

        val apple = item(0, Material.APPLE) {
            modify { name = "Running_Apple" }

            onClick { modify { amount = ++count } }
        }.add(1)

        var base = 0
        submit(now = false, async = true, 20L, 20L) {
            if (noViewer()) cancel().also {
                println("Cancelled")
            }

            apple.set(base, base + 1, base + 2)

            base++
        }
    }


}.open(player)

fun showDynamicTitle(player: Player) = bukkitChestWindow(3, "_") {

    standard(9 to 3) {
        getUnoccupiedPositions().let { posSet ->
            posSet.forEach { pos ->
                buildItem(randomMaterial()) {
                    onClick {
                        modify { amount += (if (clickType.isLeftClick) 1 else -1) }
                        player.sendMessage(pos.toString())
                    }
                }.add(pos)
            }
        }
    }

    open(player)
}.also {

    val dynamicTitles by lazy {
        var current = "_"
        val titles = mutableListOf<String>()

        "Invero Animated Title".windowed(1, 1).forEachIndexed { _, s ->
            current += s
            titles.add(current)
        }
        titles.addAll(titles.reversed())
        titles
    }

    launchAsync {
        repeating(3)

        if (!it.hasViewer()) {
            println("# return@launchAsync")
            return@launchAsync
        }
        for (title in dynamicTitles) {
            it.title = title
            yield()
        }
        it.title = "_"
    }

}