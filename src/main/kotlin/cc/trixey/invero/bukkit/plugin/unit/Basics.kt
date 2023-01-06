package cc.trixey.invero.bukkit.plugin.unit

import cc.trixey.invero.bukkit.api.dsl.*
import org.bukkit.Material
import org.bukkit.entity.Player
import taboolib.common.platform.function.submit

/**
 * @author Arasple
 * @since 2022/12/29 13:08
 */
fun showBasic(player: Player) = bukkitChestWindow(6, "Hello Invero") {

    var count = 1

    standardPanel(3 to 3) {

        item(0, Material.APPLE) {
            modify { name = "Hello Apple" }

            onClick {
                modify { amount = ++count }
                push()
            }
        }.add(1)

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