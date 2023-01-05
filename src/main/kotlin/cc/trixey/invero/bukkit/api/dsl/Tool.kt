package cc.trixey.invero.bukkit.api.dsl

import cc.trixey.invero.common.PanelContainer
import cc.trixey.invero.common.Pos

/**
 * @author Arasple
 * @since 2023/1/5 13:29
 */
fun PanelContainer.firstAvailablePositionForPanel(): Pair<Int, Int> {
    return (scale.toArea(Pos(0, 0)) - panels.flatMap { it.area }.toSet()).minBy { it.y }.value
}