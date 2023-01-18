package cc.trixey.invero.core.panel

import cc.trixey.invero.bukkit.api.dsl.firstAvailablePositionForPanel
import cc.trixey.invero.bukkit.api.dsl.standard
import cc.trixey.invero.bukkit.panel.StandardPanel
import cc.trixey.invero.common.Pos
import cc.trixey.invero.common.Scale
import cc.trixey.invero.core.AgentIcon
import cc.trixey.invero.core.AgentPanel
import cc.trixey.invero.core.Layout
import cc.trixey.invero.core.Session
import cc.trixey.invero.core.serialize.PosSerializer
import cc.trixey.invero.core.serialize.ScaleSerializer
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.json.JsonNames

/**
 * Invero
 * cc.trixey.invero.core.panel.PanelStandard
 *
 * @author Arasple
 * @since 2023/1/15 22:44
 */
@ExperimentalSerializationApi
@Serializable
class PanelStandard(
    @Serializable(with = ScaleSerializer::class)
    @SerialName("scale")
    private val _scale: Scale?,
    override val layout: Layout?,
    @Serializable(with = PosSerializer::class)
    override val locate: Pos?,
    @JsonNames("icon")
    val icons: Map<String, AgentIcon>
) : AgentPanel() {

    @Transient
    override val scale = run {
        if (_scale == null) {
            require(layout != null) { "Both scale and layout of this panel is null" }
            Scale(layout.getScale())
        } else _scale
    }

    override fun invoke(session: Session): StandardPanel {
        val window = session.viewingWindow ?: error("No window apply ge Pi")

        return window.standard(scale.raw, locate?.value ?: window.firstAvailablePositionForPanel()) {
            icons.forEach { (_, icon) -> icon.invoke(session, this@PanelStandard, this@standard) }
        }
    }

}