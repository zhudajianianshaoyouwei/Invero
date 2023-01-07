package cc.trixey.invero.bukkit.api.dsl

import cc.trixey.invero.bukkit.panel.FreeformStandardPanel
import cc.trixey.invero.common.ElementalPanel
import cc.trixey.invero.common.Panel
import cc.trixey.invero.common.PanelContainer
import cc.trixey.invero.common.Pos

/**
 * @author Arasple
 * @since 2023/1/5 13:29
 */
fun PanelContainer.firstAvailablePositionForPanel(): Pair<Int, Int> {
    return (scale.toArea() - panels.flatMap { it.area }.toSet()).minBy { it.y }.value
}

fun ElementalPanel.firstAvailablePositionForElement(): Pair<Int, Int> {
    return getUnoccupiedPositions().minByOrNull { it.y * 9 + it.x }!!.value
}

inline fun <reified T : Panel> PanelContainer.findSubPanel(): T? {
    return panels.filterIsInstance<T>().firstOrNull()
}

fun PanelContainer.firstFreeform(): FreeformStandardPanel = findSubPanel()!!
