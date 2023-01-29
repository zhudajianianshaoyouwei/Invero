@file:OptIn(ExperimentalSerializationApi::class)

package cc.trixey.invero.core.panel

import cc.trixey.invero.bukkit.PanelContainer
import cc.trixey.invero.common.Panel
import cc.trixey.invero.common.Pos
import cc.trixey.invero.common.Scale
import cc.trixey.invero.core.AgentPanel
import cc.trixey.invero.core.Layout
import cc.trixey.invero.core.Session
import cc.trixey.invero.core.icon.Icon
import cc.trixey.invero.core.panel.geneartor.Generator
import cc.trixey.invero.core.serialize.MappedIconSerializer
import cc.trixey.invero.core.serialize.PosSerializer
import cc.trixey.invero.core.serialize.ScaleSerializer
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.json.JsonNames

/**
 * Invero
 * cc.trixey.invero.core.panel.PanelGenerator
 *
 * @author Arasple
 * @since 2023/1/29 16:25
 */
@Serializable
class PanelGenerator(
    @Serializable(with = ScaleSerializer::class)
    @SerialName("scale")
    private val _scale: Scale?,
    override val layout: Layout?,
    @Serializable(with = PosSerializer::class)
    override val locate: Pos?,
    @SerialName("generator")
    val generator: Generator,
    @JsonNames("icon", "item", "items")
    @Serializable(with = MappedIconSerializer::class)
    val icons: Map<String, Icon>
) : AgentPanel() {

    @Transient
    override val scale = run {
        if (_scale == null) {
            require(layout != null) { "Both scale and layout of this panel is null" }
            Scale(layout.getScale())
        } else _scale
    }

    override fun invoke(parent: PanelContainer, session: Session): Panel {
        TODO("Not yet implemented")
    }

}