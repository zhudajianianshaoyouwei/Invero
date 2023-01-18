@file:OptIn(ExperimentalSerializationApi::class)


package cc.trixey.invero.core.serialize

import cc.trixey.invero.common.Pos
import cc.trixey.invero.common.Scale
import cc.trixey.invero.common.event.ClickType
import cc.trixey.invero.core.Layout
import cc.trixey.invero.core.MenuTitle
import cc.trixey.invero.core.action.*
import cc.trixey.invero.core.animation.CycleMode
import cc.trixey.invero.core.icon.IconHandler
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.IntArraySerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.PairSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.*
import kotlinx.serialization.serializer
import org.bukkit.event.inventory.InventoryType

/**
 * Invero_Core
 * cc.trixey.invero.serialize.Serializers
 *
 * @author Arasple
 * @since 2023/1/18 0:12
 */
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

internal object ScaleSerializer : KSerializer<Scale> {

    private val intArraySerializer = IntArraySerializer()

    override val descriptor = SerialDescriptor("Scale", intArraySerializer.descriptor)

    override fun deserialize(decoder: Decoder) =
        decoder.decodeSerializableValue(intArraySerializer).let { Scale(it[0] to it[1]) }

    override fun serialize(encoder: Encoder, value: Scale) =
        encoder.encodeSerializableValue(intArraySerializer, intArrayOf(value.width, value.height))

}

internal object LayoutSerializer : KSerializer<Layout> {

    private val listSerializer = ListSerializer(String.serializer())

    override val descriptor = SerialDescriptor("Layout", listSerializer.descriptor)

    override fun deserialize(decoder: Decoder) = Layout(decoder.decodeSerializableValue(listSerializer))

    override fun serialize(encoder: Encoder, value: Layout) = encoder.encodeSerializableValue(listSerializer, value.raw)

}

internal object PosSerializer : KSerializer<Pos> {

    private val pairSerializer = PairSerializer(Int.serializer(), Int.serializer())

    override val descriptor = SerialDescriptor("Pos", pairSerializer.descriptor)

    override fun deserialize(decoder: Decoder) = Pos(decoder.decodeSerializableValue(pairSerializer))

    override fun serialize(encoder: Encoder, value: Pos) = encoder.encodeSerializableValue(pairSerializer, value.value)

}

internal object ComparatorSerializer : KSerializer<Comparator> {

    override val descriptor = PrimitiveSerialDescriptor("Comparator", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): Comparator {
        return Comparator.parse(decoder.decodeString())
    }

    override fun serialize(encoder: Encoder, value: Comparator) {
        return encoder.encodeString(value.toString())
    }

}

internal object NetesedActionSerializer : KSerializer<NetesedAction> {

    private val listSerializer = ListSerializer(Action.serializer())
    override val descriptor = buildClassSerialDescriptor("NetesedAction") { element<List<Action>>("actions") }

    override fun serialize(encoder: Encoder, value: NetesedAction) {
        encoder.encodeSerializableValue(listSerializer, value.actions)
    }


    override fun deserialize(decoder: Decoder): NetesedAction {
        return NetesedAction(decoder.decodeSerializableValue(listSerializer))
    }

}

internal object ActionKetherSerializer : KSerializer<ActionKether> {

    private val listSerializer = ListSerializer(String.serializer())
    override val descriptor = buildClassSerialDescriptor("ActionKether") { element<String>("script") }

    override fun deserialize(decoder: Decoder) = try {
        decoder as JsonDecoder
        decoder
            .decodeSerializableValue(listSerializer)
            .joinToString("\\n") { it }
            .let { ActionKether(it) }
    } catch (e: Throwable) {
        ActionKether(decoder.decodeString())
    }

    override fun serialize(encoder: Encoder, value: ActionKether) {
        val scripts = value.script.split("\\n")
        scripts.singleOrNull()?.let { encoder.encodeString(it) }
            ?: encoder.encodeSerializableValue(listSerializer, scripts)
    }

}

internal object ScriptKetherSerializer : KSerializer<ScriptKether> {

    override val descriptor = PrimitiveSerialDescriptor("ScriptKether", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder) = ScriptKether(decoder.decodeString())

    override fun serialize(encoder: Encoder, value: ScriptKether) = encoder.encodeString(value.script)

}

internal object MenuTitleSerializer : JsonTransformingSerializer<MenuTitle>(MenuTitle.serializer()) {

    override fun transformDeserialize(element: JsonElement) =
        if (element !is JsonObject) {
            buildJsonObject { put("value", buildJsonArray { add(element) }) }
        } else element


    override fun transformSerialize(element: JsonElement) = element.let {
        val titles = it.jsonObject["value"]?.jsonArray
        if (titles?.size == 1) titles.first() else it
    }
}

internal object CycleModeSerializer : KSerializer<CycleMode> {

    override val descriptor = PrimitiveSerialDescriptor("CycleMode", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): CycleMode = decoder.decodeString().tolerantEnum(CycleMode.LOOP)

    override fun serialize(encoder: Encoder, value: CycleMode) = encoder.encodeString(value.name)

}

internal object InventoryTypeSerializer : KSerializer<InventoryType> {

    override val descriptor = PrimitiveSerialDescriptor("InventoryType", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): InventoryType = decoder.decodeString().tolerantEnum(InventoryType.CHEST)

    override fun serialize(encoder: Encoder, value: InventoryType) = encoder.encodeString(value.name)
}


private inline fun <reified T : Enum<T>> String.tolerantEnum(default: T) = uppercase()
    .replace(' ', '_')
    .replace('-', '_')
    .let { input -> enumValues<T>().find { it.name == input } ?: default }