package cc.trixey.invero.bukkit.api.dsl

import cc.trixey.invero.bukkit.panel.*
import cc.trixey.invero.common.Pos
import cc.trixey.invero.common.Scale
import cc.trixey.invero.common.panel.PanelContainer
import cc.trixey.invero.common.panel.PanelWeight

/**
 * Invero
 * cc.trixey.invero.bukkit.api.dsl.Panel
 *
 * @author Arasple
 * @since 2022/12/22 20:28
 */
inline fun PanelContainer.standard(
    scale: Pair<Int, Int> = inheritParentScale(),
    locate: Pair<Int, Int> = firstAvailablePositionForPanel(),
    weight: PanelWeight = PanelWeight.NORMAL,
    block: StandardPanel.() -> Unit
) = StandardPanel(this, weight, Scale(scale), Pos(locate)).also {
    block(it)
    this += it
}

inline fun PanelContainer.pagedNetesed(
    scale: Pair<Int, Int> = inheritParentScale(),
    locate: Pair<Int, Int> = firstAvailablePositionForPanel(),
    weight: PanelWeight = PanelWeight.NORMAL,
    block: PagedNetesedPanel.() -> Unit
) = PagedNetesedPanel(this, weight, Scale(scale), Pos(locate)).also {
    block(it)
    this += it
}

inline fun PanelContainer.group(
    scale: Pair<Int, Int> = inheritParentScale(),
    locate: Pair<Int, Int> = firstAvailablePositionForPanel(),
    weight: PanelWeight = PanelWeight.NORMAL,
    block: PanelGroup.() -> Unit
) = PanelGroup(this, weight, Scale(scale), Pos(locate)).also {
    block(it)
    this += it
}

inline fun PanelContainer.freeformPanel(
    scale: Pair<Int, Int> = inheritParentScale(),
    locate: Pair<Int, Int> = firstAvailablePositionForPanel(),
    weight: PanelWeight = PanelWeight.NORMAL,
    block: FreeformStandardPanel.() -> Unit
) = FreeformStandardPanel(this, weight, Scale(scale), Pos(locate)).also {
    block(it)
    this += it
}

inline fun PanelContainer.freeformNetesed(
    scale: Pair<Int, Int> = inheritParentScale(),
    locate: Pair<Int, Int> = firstAvailablePositionForPanel(),
    weight: PanelWeight = PanelWeight.NORMAL,
    block: FreeformNetesedPanel.() -> Unit
) = FreeformNetesedPanel(this, weight, Scale(scale), Pos(locate)).also {
    block(it)
    this += it
}