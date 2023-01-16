package cc.trixey.invero.serialize

import cc.trixey.invero.core.AgentPanel
import cc.trixey.invero.core.panel.PanelStandard
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject

/**
 * Invero
 * cc.trixey.invero.serialize.SelectorAgentPanel
 *
 * @author Arasple
 * @since 2023/1/15 22:56
 */
object SelectorAgentPanel : JsonContentPolymorphicSerializer<AgentPanel>(AgentPanel::class) {

    override fun selectDeserializer(element: JsonElement) = when {
        // generator -> PanelGeneraotr... etc
        "layout" in element.jsonObject -> PanelStandard.serializer()
        else -> PanelStandard.serializer()
    }

}