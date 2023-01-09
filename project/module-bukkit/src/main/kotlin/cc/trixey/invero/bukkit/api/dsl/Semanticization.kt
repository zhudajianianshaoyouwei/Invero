package cc.trixey.invero.bukkit.api.dsl

import cc.trixey.invero.bukkit.panel.StandardPanel
import cc.trixey.invero.common.panel.PanelContainer
import cc.trixey.invero.common.panel.PanelWeight

/**
 * @author Arasple
 * @since 2023/1/6 14:37
 */
inline fun PanelContainer.nav(
    scale: Pair<Int, Int>,
    locate: Pair<Int, Int> = firstAvailablePositionForPanel(),
    weight: PanelWeight = PanelWeight.NORMAL,
    block: StandardPanel.() -> Unit
) = standardPanel(scale, locate, weight, block)