@file:OptIn(ExperimentalSerializationApi::class)

package cc.trixey.invero.core.serialize

import cc.trixey.invero.core.AgentPanel
import cc.trixey.invero.core.Menu
import cc.trixey.invero.core.action.*
import cc.trixey.invero.core.item.Texture
import cc.trixey.invero.core.item.TextureHead
import cc.trixey.invero.core.item.TextureMaterial
import cc.trixey.invero.core.item.TextureSource
import cc.trixey.invero.core.panel.PanelGenerator
import cc.trixey.invero.core.panel.PanelPaged
import cc.trixey.invero.core.panel.PanelStandard
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
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
        return StandardMenuSerializer
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
            val keys = element.jsonObject.keys
            when {
                "head" in keys -> TextureHead.serializer()
                "source" in keys -> TextureSource.serializer()
                else -> error("Unregonized texture format: $element")
            }
        } else {
            require(element is JsonPrimitive)
            TextureMaterial.serializer()
        }
    }

}

internal object SelectorAgentPanel : JsonContentPolymorphicSerializer<AgentPanel>(AgentPanel::class) {

    override fun selectDeserializer(element: JsonElement): KSerializer<out AgentPanel> {
        val keys = element.jsonObject
        return when {
            // generator -> PanelGeneraotr... etc
            "pages" in keys -> PanelPaged.serializer()
            "generator" in keys -> PanelGenerator.serializer()
            else -> PanelStandard.serializer()
        }
    }

}