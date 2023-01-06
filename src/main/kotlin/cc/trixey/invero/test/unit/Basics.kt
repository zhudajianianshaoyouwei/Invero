package cc.trixey.invero.test.unit

import cc.trixey.invero.bukkit.api.dsl.*
import cc.trixey.invero.bukkit.util.randomMaterial
import cc.trixey.invero.util.launchAsync
import org.bukkit.Material
import org.bukkit.entity.Player
import taboolib.common.platform.function.submit

/**
 * @author Arasple
 * @since 2022/12/29 13:08
 */
fun showBasic(player: Player) = bukkitChestWindow(6, "Hello TestPlugin") {

    var count = 1

    standardPanel(3 to 3) {

        item(0, Material.APPLE) {
            modify { name = "Hello Apple" }

            onClick {
                modify { amount = ++count }
                push()
            }
        }.add(1)

        buildItem(Material.DIAMOND).fillup().onClick {
            isCancelled = false
        }
    }

    standardPanel(3 to 7) {
        buildItem(Material.EMERALD).fillup().onClick {
            player.sendMessage("6")
        }
    }


}.open(player)

fun showRunningApple(player: Player) = bukkitChestWindow(6, "Running apple") {

    var count = 1

    standardPanel(9 to 6 + 4) {

        val apple = item(0, Material.APPLE) {
            modify { name = "Running_Apple" }

            onClick {
                modify { amount = ++count }
                push()
            }
        }.add(1)

        var base = 0
        submit(now = false, async = true, 20L, 20L) {
            if (!hasViewer()) cancel().also {
                println("Cancelled")
            }

            apple.set(base, base + 1, base + 2)
            apple.push()

            base++
        }
    }


}.open(player)

fun showDynamicTitle(player: Player) = bukkitChestWindow(3, "_") {

    standardPanel(9 to 3) {
        getUnoccupiedPositions().let { posSet ->
            posSet.forEach { pos ->
                buildItem(randomMaterial()) {
                    onClick {
                        modify { amount += (if (clickType.isLeftClick) 1 else -1) }
                        player.sendMessage(pos.toString())
                        push()
                    }
                }.add(pos)
            }
        }
    }

    open(player)
}.also {

    val dynamicTitles by lazy {
        var current = ""
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