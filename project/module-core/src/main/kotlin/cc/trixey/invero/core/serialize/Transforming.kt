package cc.trixey.invero.core.serialize

import cc.trixey.invero.common.event.ClickType
import cc.trixey.invero.core.icon.Icon
import cc.trixey.invero.core.icon.IconHandler
import cc.trixey.invero.core.menu.CommandArgument
import cc.trixey.invero.core.menu.StandardMenu
import kotlinx.serialization.json.*
import kotlinx.serialization.serializer

/**
 * Invero
 * cc.trixey.invero.core.serialize.Transforming
 *
 * @author Arasple
 * @since 2023/1/29 13:43
 */
internal object StandardMenuSerializer : JsonTransformingSerializer<StandardMenu>(serializer()) {

    private val menuKeys = arrayOf(
        "title",
        "rows",
        "virtual",
        "type",
        "override-player-inventory",
        "hidePlayerStorage"
    )

    private val panelKeys = arrayOf(
        "layout",
        "locate",
        "scale",
        "pages",
        "generator",
        "items", "icons", "item", "icon"
    )

    override fun transformDeserialize(element: JsonElement): JsonElement {
        val struc = element.jsonObject.toMutableMap()

        if ("menu" !in struc.keys) {
            struc["menu"] = buildJsonObject {
                menuKeys.forEach { key ->
                    struc[key]?.let { value -> put(key, value) }
                }
            }
        }

        if (arrayOf("panel", "panels").none { it in struc.keys } && "layout" in struc.keys) {
            struc["panel"] = buildJsonObject {
                panelKeys.forEach { key ->
                    struc[key]?.let { value -> put(key, value) }
                }
            }
        }

        return JsonObject(struc)
    }

}

internal object IconSerializer : JsonTransformingSerializer<Icon>(serializer()) {

    private val displayKeys = arrayOf(
        "material", "texture", "mat",
        "name",
        "lore", "lores",
        "amount", "count", "amt",
        "damage",
        "customModelData", "model",
        "color",
        "glow",
        "enchantments",
        "flags",
        "unbreakable",
        "nbt"
    )

    private val textureKeysHead = arrayOf(
        "head", "skull"
    )

    private val textureSourcedKeys = arrayOf(
        "zaphkiel", "zap",
        "oraxen",
        "itemsadder", "ia",
        "headdatabase", "hdb"
    )

    override fun transformDeserialize(element: JsonElement): JsonElement {
        val struc = element.jsonObject.toMutableMap()

        if ("display" !in struc.keys) {
            var textureSpecified = false
            val texture = buildJsonObject {
                // head
                val head = textureKeysHead.any { key ->
                    val value = struc[key]
                    value?.let { put("head", value) }
                    value != null
                }
                // sourced
                val source = textureSourcedKeys.any { key ->
                    val value = struc[key]
                    value?.let {
                        put("source", key.uppercase())
                        put("value", value)
                    }
                    value != null
                }
                textureSpecified = head || source
            }

            struc["display"] = buildJsonObject {
                displayKeys.forEach { key -> struc[key]?.let { value -> put(key, value) } }
                if (textureSpecified) put("texture", texture)
            }
        }
        return JsonObject(struc)
    }

    override fun transformSerialize(element: JsonElement): JsonElement {
        val struc = element.jsonObject.toMutableMap()
        struc["display"]!!.jsonObject.forEach { key, value ->
            struc[key] = value
        }
        struc.remove("display")
        return JsonObject(struc)
    }

}

internal object CommandArgumentSerailizer : JsonTransformingSerializer<CommandArgument>(serializer()) {

    override fun transformDeserialize(element: JsonElement): JsonElement {
        return if (element is JsonPrimitive) {
            buildJsonObject {
                put("id", element.content)
                put("type", "ANY")
            }
        } else {
            element
        }
    }

}

internal object IconHandlerSerializer : JsonTransformingSerializer<IconHandler>(serializer()) {

    override fun transformDeserialize(element: JsonElement): JsonElement {
        return when (element) {
            is JsonPrimitive, is JsonArray -> {
                buildJsonObject { put("all", element) }
            }

            is JsonObject -> {
                val jsonObject = element.jsonObject

                if (arrayOf("if", "when").any { it in jsonObject }) {
                    return buildJsonObject { put("all", jsonObject) }
                }

                buildJsonObject {
                    jsonObject["all"]?.let { put("all", it) }

                    buildJsonObject {
                        jsonObject.keys.filterNot { it == "all" }.forEach { key ->
                            val type = ClickType.values()
                                .find { it.name.equals(key, true) || it.bukkitId.equals(key, true) }
                            val action = jsonObject[key]
                            if (type != null && action != null) put(type.name, action)
                        }
                    }.let {
                        put("response", it)
                    }
                }
            }
        }
    }

    override fun transformSerialize(element: JsonElement): JsonElement {
        val all = element.jsonObject["all"]
        val specificed = element.jsonObject["response"]

        return if ((all is JsonPrimitive || all is JsonArray) && specificed?.jsonObject.isNullOrEmpty()) all else element
    }

}