package cc.trixey.invero.core.panel

import cc.trixey.invero.bukkit.api.dsl.firstAvailablePositionForPanel
import cc.trixey.invero.bukkit.api.dsl.standard
import cc.trixey.invero.bukkit.panel.StandardPanel
import cc.trixey.invero.common.Pos
import cc.trixey.invero.common.Scale
import cc.trixey.invero.core.AgentIcon
import cc.trixey.invero.core.AgentPanel
import cc.trixey.invero.core.Layout
import cc.trixey.invero.core.session.Session
import cc.trixey.invero.serialize.SerializerLayout
import cc.trixey.invero.serialize.SerializerPos
import cc.trixey.invero.serialize.SerializerScale
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
@OptIn(ExperimentalSerializationApi::class)
@Serializable
class PanelStandard(
    @Serializable(with = SerializerScale::class)
    @SerialName("scale")
    private val _scale: Scale? = null,
    @Serializable(with = SerializerLayout::class)
    override val layout: Layout? = null,
    @Serializable(with = SerializerPos::class)
    override val locate: Pos? = null,
    @JsonNames("icon", "item", "items")
    val icons: Map<String, AgentIcon>
) : AgentPanel() {

    @Transient
    override val scale = run {
        if (_scale == null) {
            require(layout != null) { "Both scale and layout of this panel is null" }
            Scale(layout.getScale())
        } else {
            _scale
        }
    }

    override fun invoke(session: Session): StandardPanel {
        val window = session.viewingWindow ?: error("No window apply ge Pi")

        return window.standard(scale.raw, locate?.value ?: window.firstAvailablePositionForPanel()) {
            icons.forEach { (_, icon) ->
                icon.invoke(session, this@PanelStandard, this@standard)
            }
        }
    }

}