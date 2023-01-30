@file:OptIn(ExperimentalSerializationApi::class)

package cc.trixey.invero.core.panel

import cc.trixey.invero.bukkit.PanelContainer
import cc.trixey.invero.bukkit.api.dsl.generatorPaged
import cc.trixey.invero.common.Panel
import cc.trixey.invero.common.Pos
import cc.trixey.invero.common.Scale
import cc.trixey.invero.core.AgentPanel
import cc.trixey.invero.core.Layout
import cc.trixey.invero.core.Session
import cc.trixey.invero.core.geneartor.GeneratorSettings
import cc.trixey.invero.core.geneartor.Object
import cc.trixey.invero.core.icon.Icon
import cc.trixey.invero.core.serialize.MappedIconSerializer
import cc.trixey.invero.core.serialize.PosSerializer
import cc.trixey.invero.core.serialize.ScaleSerializer
import cc.trixey.invero.core.util.KetherHandler
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.json.JsonNames
import org.bukkit.entity.Player
import taboolib.common5.cbool

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
    @SerialName("generator") val settings: GeneratorSettings,
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
        return parent.generatorPaged(scale.raw, parent.locate()) {
            // 生成默认图标
            icons.forEach { (_, icon) ->
                icon.invoke(session, this@PanelGenerator, this@generatorPaged)
            }
            // 应用元素
            generatorElements { genearte(session) }
            // 生成输出
            onGenerate { settings.output.invoke(session, this@PanelGenerator, this, mapOf("@element" to it)) }
        }
    }

    private fun genearte(session: Session): List<Object> {
        val viewer = session.viewer.get<Player>()
        val created = settings.create().apply {
            generate()
            if (settings.extenedObjects != null) {
                generated = generated!! + settings.extenedObjects
            }
            if (settings.extenedProperties != null) {
                generated = generated?.map {
                    val content = it.content.toMutableMap()
                    settings.extenedProperties.forEach { (key, value) -> content[key] = "ext.kether:$value" }
                    Object(content)
                }
            }
            if (settings.filter != null) {
                filter { KetherHandler.invoke(settings.filter, viewer, mapOf("@element" to it)).getNow(true).cbool }
            }
            if (settings.sortBy != null) {
                sortBy { KetherHandler.invoke(settings.sortBy, viewer, mapOf("@element" to it)).getNow(null).toString() }
            }
        }
        return created.generated!!
    }

}