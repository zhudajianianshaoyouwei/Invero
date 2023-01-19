package cc.trixey.invero.core.panel

import cc.trixey.invero.bukkit.api.dsl.firstAvailablePositionForPanel
import cc.trixey.invero.bukkit.api.dsl.pagedNetesed
import cc.trixey.invero.bukkit.panel.PagedNetesedPanel
import cc.trixey.invero.common.Pos
import cc.trixey.invero.common.Scale
import cc.trixey.invero.common.panel.PanelContainer
import cc.trixey.invero.core.AgentPanel
import cc.trixey.invero.core.Layout
import cc.trixey.invero.core.Session
import cc.trixey.invero.core.serialize.ListAgentPanelSerializer
import cc.trixey.invero.core.serialize.PosSerializer
import cc.trixey.invero.core.serialize.ScaleSerializer
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

/**
 * Invero
 * cc.trixey.invero.core.panel.PanelPaged
 *
 * @author Arasple
 * @since 2023/1/19 16:56
 */
@ExperimentalSerializationApi
@Serializable
class PanelPaged(
    @Serializable(with = ScaleSerializer::class)
    override val scale: Scale,
    @Serializable(with = PosSerializer::class)
    override val locate: Pos?,
    @SerialName("default-page")
    @JsonNames("default", "def-page")
    val defaultPage: Int = 0,
    @Serializable(with = ListAgentPanelSerializer::class)
    val pages: List<AgentPanel>
) : AgentPanel() {

    override val layout: Layout? = null

    init {
        require(defaultPage in pages.indices) { "Default Page ($defaultPage) is out of indices ${pages.indices}" }
    }

    override fun invoke(parent: PanelContainer, session: Session): PagedNetesedPanel {
        return parent.pagedNetesed(scale.raw, locate?.value ?: parent.firstAvailablePositionForPanel()) {
            pages[defaultPage].invoke(this, session)
            onPageChanging { _, toPage ->
                if (toPage in pages.indices) pages[toPage].invoke(this@pagedNetesed, session)
            }
        }
    }

}