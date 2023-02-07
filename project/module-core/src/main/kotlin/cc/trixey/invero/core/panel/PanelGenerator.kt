@file:OptIn(ExperimentalSerializationApi::class)

package cc.trixey.invero.core.panel

import cc.trixey.invero.common.supplier.Object
import cc.trixey.invero.core.AgentPanel
import cc.trixey.invero.core.BaseMenu
import cc.trixey.invero.core.Layout
import cc.trixey.invero.core.Session
import cc.trixey.invero.core.icon.Icon
import cc.trixey.invero.core.serialize.MappedIconSerializer
import cc.trixey.invero.core.serialize.PosSerializer
import cc.trixey.invero.core.serialize.ScaleSerializer
import cc.trixey.invero.core.util.KetherHandler
import cc.trixey.invero.ui.bukkit.PanelContainer
import cc.trixey.invero.ui.bukkit.api.dsl.generatorPaged
import cc.trixey.invero.ui.bukkit.panel.PagedGeneratorPanel
import cc.trixey.invero.ui.common.Panel
import cc.trixey.invero.ui.common.Pos
import cc.trixey.invero.ui.common.Scale
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.json.JsonNames
import org.bukkit.entity.Player
import taboolib.common.platform.function.submit
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
    @SerialName("generator")
    val settings: GeneratorStructure,
    @JsonNames("icon", "item", "items")
    @Serializable(with = MappedIconSerializer::class)
    override val icons: Map<String, Icon>
) : AgentPanel(), IconContainer {

    init {
        registerIcon(settings.output, "output")
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
            skipRender = true

            // 生成默认图标
            val def = icons.map { (_, icon) ->
                icon.invoke(session, this@PanelGenerator, this@generatorPaged, renderNow = false)
            }
            // 应用元素
            generatorSource { genearte() }
            // 过滤元素
            if (settings.filter != null) {
                filter(session, this@generatorPaged, settings.filter)
                session.setVariable("@raw_filter", settings.filter)
            }
            // 生成输出
            generatorOutput {
                settings.output.invoke(session, this@PanelGenerator, this, (it as Object).variables)
            }
            submit(delay = 1L) {
                render()
                def.forEach {
                    it.relocate()
                    it.render()
                }
                (session.menu as BaseMenu).updateTitle(session)
            }
        }
    }

    fun filter(session: Session, panel: PagedGeneratorPanel<*>, filter: String) {
        val viewer = session.viewer.get<Player>()
        panel.filterBy {
            KetherHandler
                .invoke(filter, viewer, session.getVariables(ext = (it as Object).variables))
                .getNow(true)
                .cbool
        }
    }

    private fun genearte(): List<Object> {
        val created = settings.create().apply {
            generate()
            if (settings.extenedObjects != null) {
                generated = generated!! + settings.extenedObjects
            }
            if (settings.extenedProperties != null) {
                generated = generated?.map {
                    val content = it.content.toMutableMap()
                    settings.extenedProperties.forEach { (key, value) -> content[key] = "ext@$value" }
                    Object(content)
                }
            }
            if (settings.sortBy != null) {
                sortBy { it[settings.sortBy].toString() }
            }
        }
        return created.generated!!
    }

}