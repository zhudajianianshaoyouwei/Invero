package cc.trixey.invero.core

import cc.trixey.invero.common.Panel
import cc.trixey.invero.core.icon.IconElement
import cc.trixey.invero.core.serialize.SelectorAgentIcon
import kotlinx.serialization.Serializable

/**
 * Invero
 * cc.trixey.invero.core.IconAgent
 *
 * @author Arasple
 * @since 2023/1/16 9:54
 */
@Serializable(with = SelectorAgentIcon::class)
abstract class AgentIcon {

    abstract val id: String?

    abstract fun invoke(session: Session, agent: AgentPanel, panel: Panel): IconElement

}