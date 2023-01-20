package cc.trixey.invero.core

import cc.trixey.invero.bukkit.PanelContainer
import cc.trixey.invero.common.Panel
import cc.trixey.invero.common.Pos
import cc.trixey.invero.common.Scale
import cc.trixey.invero.core.serialize.SelectorAgentPanel
import kotlinx.serialization.Serializable

/**
 * Invero
 * cc.trixey.invero.core.MenuPanels
 *
 * @author Arasple
 * @since 2023/1/15 22:35
 */
@Serializable(with = SelectorAgentPanel::class)
abstract class AgentPanel {

    abstract val scale: Scale

    abstract val layout: Layout?

    abstract val locate: Pos?

    abstract fun invoke(parent: PanelContainer, session: Session): Panel

    fun requireBukkitWindow(): Boolean {
        return false
    }

}