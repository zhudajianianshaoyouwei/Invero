package cc.trixey.invero.plugin.demo

import cc.trixey.invero.bukkit.api.dsl.*
import cc.trixey.invero.bukkit.util.randomMaterial
import cc.trixey.invero.common.Pos
import cc.trixey.invero.common.panel.PanelContainer
import org.bukkit.Material
import org.bukkit.entity.Player
import taboolib.common.platform.function.submit

/**
 * Invero
 * cc.trixey.invero.plugin.demo.Freeform
 *
 * @author Arasple
 * @since 2023/1/6 14:45
 */
fun showFreeformStandard(player: Player) = packetChestWindow(6, "FreeformPanel Standard") {

    freeformPanel(9 to 6) {

        /*
        占据 0,0 到 5,5 的所有区域
         */
        for (x in 0..5)
            for (y in 0..5)
                buildItem(randomMaterial()) {
                    onClick { player.sendMessage("Build on $x to $y") }
                }.add(Pos(x to y))

        /*
        从 6，0 开始沿 4 格长的斜线 来回跑的一个苹果
         */
        val startX = 6
        val startY = 0
        var appleCount = 1
        var count = 0
        var reversing = false
        val runningApple = item(startX to startY, Material.APPLE) {
            modify { name = "Running_Apple" }

            onClick { modify { amount = ++appleCount } }
        }

        submit(now = false, async = true, 20L, 20L) {
            if (noViewer()) cancel().also {
                println("Cancelled")
            }

            runningApple.set(Pos((startX + count) to (startY + count)))

            if (count >= 4 && !reversing) reversing = true
            if (reversing && count <= 0) reversing = false

            if (reversing) count--
            else count++
        }
    }

    freeformNavigator()

    open(player)
}

fun PanelContainer.freeformNavigator(locate: Pair<Int, Int> = 3 to 6) {
    nav(3 to 3, locate) {
        item(org.bukkit.Material.GRAY_STAINED_GLASS_PANE) {
            modify { name = "↖" }
            onClick { firstFreeform().shiftUpLeft() }
        }
        item(org.bukkit.Material.BLUE_STAINED_GLASS_PANE) {
            modify { name = "↑" }
            onClick { firstFreeform().shiftUp() }
        }
        item(org.bukkit.Material.GRAY_STAINED_GLASS_PANE) {
            modify { name = "↗" }
            onClick { firstFreeform().shiftUpRight() }
        }
        item(org.bukkit.Material.RED_STAINED_GLASS_PANE) {
            modify { name = "←" }
            onClick { firstFreeform().shiftLeft() }
        }
        item(org.bukkit.Material.BLACK_STAINED_GLASS_PANE) {
            modify { name = "RESET" }
            onClick { firstFreeform().resetViewport() }
        }
        item(org.bukkit.Material.GREEN_STAINED_GLASS_PANE) {
            modify { name = "→" }
            onClick { firstFreeform().shiftRight() }
        }
        item(org.bukkit.Material.GRAY_STAINED_GLASS_PANE) {
            modify { name = "↙" }
            onClick { firstFreeform().shiftDownLeft() }
        }
        item(org.bukkit.Material.ORANGE_STAINED_GLASS_PANE) {
            modify { name = "↓" }
            onClick { firstFreeform().shiftDown() }
        }
        item(org.bukkit.Material.ORANGE_STAINED_GLASS_PANE) {
            modify { name = "↘" }
            onClick { firstFreeform().shiftDownRight() }
        }
    }
}