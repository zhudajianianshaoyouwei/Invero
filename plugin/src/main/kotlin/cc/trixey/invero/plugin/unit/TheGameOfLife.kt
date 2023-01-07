package cc.trixey.invero.plugin.unit

import cc.trixey.invero.bukkit.api.dsl.*
import cc.trixey.invero.bukkit.element.SimpleItem
import cc.trixey.invero.bukkit.nms.updateTitle
import cc.trixey.invero.common.PanelWeight
import cc.trixey.invero.common.Pos
import org.bukkit.Material
import org.bukkit.entity.Player
import taboolib.common.platform.function.submitAsync

/**
 * @author Arasple
 * @since 2023/1/7 13:30
 */

private const val CAPACITY = 200
private const val PERIOD = 20L
private val cells = mutableSetOf<Pos>()
private var state: Boolean = false
private var generation = 0

fun showTheGameOfLife(player: Player) = bukkitChestWindow(6, formattedTitle()) {

    // 细胞物品
    val cell: SimpleItem by lazy {
        firstFreeform().buildItem(Material.SLIME_BALL) {
            modify { name = "§aCELL" }
        }
    }

    // 每个周期刷新
    fun refresh() {
        updateTitle(formattedTitle())
//        cell.modify { amount = cells.size.coerceAtMost(64) }
        cell.set(cells)
        cell.push()
    }

    // 周期任务
    submitAsync(false, 20L, PERIOD) {

        if (noViewer()) {
            stop(true)
            return@submitAsync cancel()
        }

        if (state) {

            val death = cells
                .mapNotNull {
                    val nearbyAlive = it.nearbyAlive()

                    if (nearbyAlive < 2 || nearbyAlive > 3) it
                    else null
                }

            val birth = cells
                .flatMap { it.nearBy() }
                .filter { it !in cells }
                .mapNotNull {
                    val nearbyAlive = it.nearbyAlive()

                    if (nearbyAlive == 3) it
                    else null
                }

            cells -= death.toSet()
            cells += birth
            generation++

            refresh()

            if (cells.size >= CAPACITY) {
                stop(true)
                refresh()
            }
        }
    }

    freeformPanel(9 to 6) {
        onClick {
            if (!state) {
                cells += toAbsolutePosition(rawSlot)
                refresh()
            }
        }
    }

    standardPanel(9 to 1, 0 to 7, PanelWeight.BACKGROUND) {
        item(1, Material.REDSTONE_TORCH) {
            modify { name = "SWITCH" }
            onClick {
                state = !state
                generation = 0
                refresh()
            }
        }
    }

    nav(3 to 3, 3 to 6) {
        item(Material.GRAY_STAINED_GLASS_PANE) {
            modify { name = "↖" }
            onClick { firstFreeform().upLeft() }
        }
        item(Material.BLUE_STAINED_GLASS_PANE) {
            modify { name = "↑" }
            onClick { firstFreeform().up() }
        }
        item(Material.GRAY_STAINED_GLASS_PANE) {
            modify { name = "↗" }
            onClick { firstFreeform().upRight() }
        }
        item(Material.RED_STAINED_GLASS_PANE) {
            modify { name = "←" }
            onClick { firstFreeform().left() }
        }
        item(Material.BLACK_STAINED_GLASS_PANE) {
            modify { name = "RESET" }
            onClick { firstFreeform().reset() }
        }
        item(Material.GREEN_STAINED_GLASS_PANE) {
            modify { name = "→" }
            onClick { firstFreeform().right() }
        }
        item(Material.GRAY_STAINED_GLASS_PANE) {
            modify { name = "↙" }
            onClick { firstFreeform().downLeft() }
        }
        item(Material.ORANGE_STAINED_GLASS_PANE) {
            modify { name = "↓" }
            onClick { firstFreeform().down() }
        }
        item(Material.ORANGE_STAINED_GLASS_PANE) {
            modify { name = "↘" }
            onClick { firstFreeform().downRight() }
        }
    }

    open(player)
}

private fun Pos.nearbyAlive() = nearBy().count { cells.contains(it) }

private fun Pos.nearBy() =
    setOf(Pos(x + 1, y), Pos(x + 1, y + 1), Pos(x + 1, y - 1), Pos(x - 1, y), Pos(x - 1, y + 1), Pos(x - 1, y - 1), Pos(x, y + 1), Pos(x, y - 1))


private fun formattedTitle() = "(State: $state) Gen#${generation} / Cells: ${cells.size}"

private fun stop(clean: Boolean = false) {
    state = false
    generation = 0
    if (clean) cells.clear()
}