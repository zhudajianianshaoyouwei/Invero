package cc.trixey.invero.bukkit.api.dsl

import cc.trixey.invero.bukkit.panel.FreeformStandardPanel
import cc.trixey.invero.bukkit.panel.PagedNetesedPanel
import cc.trixey.invero.common.Panel
import cc.trixey.invero.common.panel.ElementalPanel
import cc.trixey.invero.common.panel.PagedPanel
import cc.trixey.invero.common.panel.PanelContainer

/**
 * @author Arasple
 * @since 2023/1/5 13:29
 */
fun PanelContainer.firstAvailablePositionForPanel(): Pair<Int, Int> {
    // pagedNetesedPanel
    if (this is PagedPanel) return 0 to 0

    val available = scale.getArea() - panels.flatMap { it.area }.toSet()
    return available.minBy { it.y }.value
}

fun ElementalPanel.firstAvailablePositionForElement(): Pair<Int, Int> {
    return getUnoccupiedPositions().minByOrNull { it.y * 9 + it.x }!!.value
}

inline fun <reified T : Panel> PanelContainer.findSubPanel(): T? {
    return panels.filterIsInstance<T>().firstOrNull()
}

fun PanelContainer.firstFreeform(): FreeformStandardPanel = findSubPanel()!!

fun PanelContainer.firstPagedNetesed(): PagedNetesedPanel = findSubPanel()!!
