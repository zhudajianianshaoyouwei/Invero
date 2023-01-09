package cc.trixey.invero.bukkit.api.dsl

import cc.trixey.invero.bukkit.panel.FreeformStandardPanel
import cc.trixey.invero.bukkit.panel.PagedNetesedPanel
import cc.trixey.invero.bukkit.panel.StandardPanel
import cc.trixey.invero.common.Pos
import cc.trixey.invero.common.Scale
import cc.trixey.invero.common.panel.PanelContainer
import cc.trixey.invero.common.panel.PanelWeight

/**
 * @author Arasple
 * @since 2022/12/22 20:28
 */
inline fun PanelContainer.standardPanel(
    scale: Pair<Int, Int> = this.scale.raw,
    locate: Pair<Int, Int> = firstAvailablePositionForPanel(),
    weight: PanelWeight = PanelWeight.NORMAL,
    block: StandardPanel.() -> Unit
) {
    this += StandardPanel(this, weight, Scale(scale), Pos(locate)).also(block)
}

inline fun PanelContainer.pagedNetesed(
    scale: Pair<Int, Int> = this.scale.raw,
    locate: Pair<Int, Int> = firstAvailablePositionForPanel(),
    weight: PanelWeight = PanelWeight.NORMAL,
    block: PagedNetesedPanel.() -> Unit
) {
    this += PagedNetesedPanel(this, weight, Scale(scale), Pos(locate)).also(block)
}

inline fun PanelContainer.freeformPanel(
    scale: Pair<Int, Int> = this.scale.raw,
    locate: Pair<Int, Int> = firstAvailablePositionForPanel(),
    weight: PanelWeight = PanelWeight.NORMAL,
    block: FreeformStandardPanel.() -> Unit
) {
    this += FreeformStandardPanel(this, weight, Scale(scale), Pos(locate)).also(block)
}