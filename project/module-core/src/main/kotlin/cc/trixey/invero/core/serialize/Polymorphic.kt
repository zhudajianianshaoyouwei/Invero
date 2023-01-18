package cc.trixey.invero.core.serialize

import cc.trixey.invero.core.AgentIcon
import cc.trixey.invero.core.AgentPanel
import cc.trixey.invero.core.action.*
import cc.trixey.invero.core.icon.Icon
import cc.trixey.invero.core.item.Texture
import cc.trixey.invero.core.item.TextureHead
import cc.trixey.invero.core.item.TextureMaterial
import cc.trixey.invero.core.panel.PanelStandard
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.json.*

/**
 * Invero_Core
 * cc.trixey.invero.serialize.Polymorphic
 *
 * @author Arasple
 * @since 2023/1/18 10:47
 */
internal object SelectorAction : JsonContentPolymorphicSerializer<Action>(Action::class) {

    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<out Action> {
        return if (element is JsonPrimitive) {
            ActionKether.serializer()
        } else if (element is JsonArray) {
            if (element.jsonArray.firstOrNull() is JsonPrimitive) ActionKether.serializer()
            else NetesedAction.serializer()
        } else {
            val primaryKeys = element.jsonObject.keys
            when {
                "if" in primaryKeys -> StructureActionTertiary.serializer()
                "when" in primaryKeys -> StructureActionWhen.serializer()
                else -> error("unregonized action [$primaryKeys]")
            }
        }
    }

}

internal object SelectorTexture : JsonContentPolymorphicSerializer<Texture>(Texture::class) {

    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<out Texture> {
        return if (element is JsonObject) {
            when {
                "head" in element.jsonObject -> TextureHead.serializer()
                "source" in element.jsonObject -> TextureHead.serializer()
                else -> error("Unregonized texture format: $element")
            }
        } else {
            TextureMaterial.serializer()
        }
    }

}

internal object SelectorAgentIcon : JsonContentPolymorphicSerializer<AgentIcon>(AgentIcon::class) {

    override fun selectDeserializer(element: JsonElement) = when {
        "frame" in element.jsonObject -> Icon.serializer()
        else -> Icon.serializer()
    }

}

internal object SelectorAgentPanel : JsonContentPolymorphicSerializer<AgentPanel>(AgentPanel::class) {

    override fun selectDeserializer(element: JsonElement) = when {
        // generator -> PanelGeneraotr... etc
        "layout" in element.jsonObject -> PanelStandard.serializer()
        else -> PanelStandard.serializer()
    }

}