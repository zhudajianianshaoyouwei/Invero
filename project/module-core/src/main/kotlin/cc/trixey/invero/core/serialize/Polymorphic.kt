@file:OptIn(ExperimentalSerializationApi::class)

package cc.trixey.invero.core.serialize

import cc.trixey.invero.core.AgentIcon
import cc.trixey.invero.core.AgentPanel
import cc.trixey.invero.core.Menu
import cc.trixey.invero.core.menu.StandardMenu
import cc.trixey.invero.core.action.*
import cc.trixey.invero.core.icon.Icon
import cc.trixey.invero.core.item.Texture
import cc.trixey.invero.core.item.TextureHead
import cc.trixey.invero.core.item.TextureMaterial
import cc.trixey.invero.core.panel.PanelPaged
import cc.trixey.invero.core.panel.PanelStandard
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.*

/**
 * Invero
 * cc.trixey.invero.serialize.Polymorphic
 *
 * @author Arasple
 * @since 2023/1/18 10:47
 */
internal object SelectorMenu : JsonContentPolymorphicSerializer<Menu>(Menu::class) {

    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<out Menu> {
        return StandardMenu.serializer()
    }

}

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
                "if" in primaryKeys -> StructureActionIf.serializer()
                "if not" in primaryKeys || "if_not" in primaryKeys -> StructureActionIfNot.serializer()
                "when" in primaryKeys -> StructureActionWhen.serializer()
                "kether" in primaryKeys -> StructureActionKether.serializer()
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
            // head:{{player name}}  SUPPORT
            require(element is JsonPrimitive)
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
        "pages" in element.jsonObject -> PanelPaged.serializer()
        else -> PanelStandard.serializer()
    }

}