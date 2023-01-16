package cc.trixey.invero.serialize

import cc.trixey.invero.core.AgentIcon
import cc.trixey.invero.core.icon.Icon
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject

/**
 * Invero
 * cc.trixey.invero.serialize.SelectorAgentIcon
 *
 * @author Arasple
 * @since 2023/1/16 10:26
 */
object SelectorAgentIcon : JsonContentPolymorphicSerializer<AgentIcon>(AgentIcon::class) {

    override fun selectDeserializer(element: JsonElement) = when {
        "frame" in element.jsonObject -> Icon.serializer()
        else -> Icon.serializer()
    }

}