package cc.trixey.invero.plugin.demo

import cc.trixey.invero.ui.bukkit.api.dsl.*
import cc.trixey.invero.ui.bukkit.element.item.SimpleItem
import cc.trixey.invero.ui.bukkit.nms.updateTitle
import cc.trixey.invero.ui.common.Pos
import cc.trixey.invero.ui.common.panel.PanelWeight
import org.bukkit.Material
import org.bukkit.entity.Player
import taboolib.common.platform.function.submitAsync

/**
 * Invero
 * cc.trixey.invero.plugin.demo.Freeform_GameOfLife
 *
 * @author Arasple
 * @since 2023/1/7 13:30
 */

private const val CAPACITY = 200
private const val PERIOD = 20L
private val cells = mutableSetOf<Pos>()
private var state: Boolean = false
private var generation = 0

fun showTheGameOfLife(player: Player) = chestWindow(player.viewer, 6, formattedTitle()) {

    // 细胞物品
    val cell: SimpleItem by lazy {
        firstFreeformStandard().buildItem(Material.SLIME_BALL) {
            modify { name = "§aCELL" }
        }
    }

    // 每个周期刷新
    fun refresh() {
        updateTitle(formattedTitle())
        cell.set(cells)
    }

    // 周期任务
    submitAsync(false, 20L, PERIOD) {

        if (!isViewing()) {
            stop(true)
            return@submitAsync cancel()
        }

        if (state) {

            val death = cells.mapNotNull {
                val nearbyAlive = it.nearbyAlive()

                if (nearbyAlive < 2 || nearbyAlive > 3) it
                else null
            }

            val birth = cells.flatMap { it.nearBy() }.filter { it !in cells }.mapNotNull {
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
        onClick { pos, _, _ ->
            if (!state) {
                cells += pos.absolute
                refresh()
                false
            } else true
        }
    }

    standard(9 to 1, 0 to 7, PanelWeight.BACKGROUND) {
        item(1, Material.REDSTONE_TORCH) {
            modify { name = "SWITCH" }
            click {
                state = !state
                generation = 0
                refresh()
            }
        }
    }

    freeformNavigator()

    open()
}

private fun Pos.nearbyAlive() = nearBy().count { cells.contains(it) }

private fun Pos.nearBy() = setOf(
    Pos(x + 1, y),
    Pos(x + 1, y + 1),
    Pos(x + 1, y - 1),
    Pos(x - 1, y),
    Pos(x - 1, y + 1),
    Pos(x - 1, y - 1),
    Pos(x, y + 1),
    Pos(x, y - 1)
)

private fun formattedTitle() = "(State: $state) Gen#${generation} / Cells: ${cells.size}"

private fun stop(clean: Boolean = false) {
    state = false
    generation = 0
    if (clean) cells.clear()
}