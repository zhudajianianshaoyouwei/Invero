package cc.trixey.invero.core.panel

import cc.trixey.invero.core.AgentPanel
import cc.trixey.invero.core.Layout
import cc.trixey.invero.core.Session
import cc.trixey.invero.core.icon.Icon
import cc.trixey.invero.core.serialize.MappedIconSerializer
import cc.trixey.invero.core.serialize.PosSerializer
import cc.trixey.invero.core.serialize.ScaleSerializer
import cc.trixey.invero.ui.bukkit.PanelContainer
import cc.trixey.invero.ui.bukkit.api.dsl.standard
import cc.trixey.invero.ui.common.Panel
import cc.trixey.invero.ui.common.Pos
import cc.trixey.invero.ui.common.Scale
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
    @JsonNames("icon", "item", "items")
    @Serializable(with = MappedIconSerializer::class)
    override val icons: Map<String, Icon>
) : AgentPanel(), IconContainer {

    init {
        registerIcons()
    }

    @Transient
    override val scale = run {
        if (_scale == null) {
            require(layout != null) { "Both scale and layout of this panel is null" }
            Scale(layout.getScale())
        } else _scale
    }

    override fun invoke(parent: PanelContainer, session: Session): Panel {
        return parent.standard(scale.raw, parent.locate()) {
            icons.forEach { (_, icon) -> icon.invoke(session, this@PanelStandard, this@standard) }
        }
    }

}