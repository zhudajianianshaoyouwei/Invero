@file:OptIn(ExperimentalSerializationApi::class)

package cc.trixey.invero.core.serialize

import cc.trixey.invero.common.Pos
import cc.trixey.invero.common.Scale
import cc.trixey.invero.core.Layout
import cc.trixey.invero.core.action.*
import cc.trixey.invero.core.animation.CycleMode
import cc.trixey.invero.core.menu.MenuTitle
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.IntArraySerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.*
import org.bukkit.Sound
import org.bukkit.event.inventory.InventoryType

/**
 * Invero
 * cc.trixey.invero.serialize.Serializers
 *
 * @author Arasple
 * @since 2023/1/18 0:12
 */

internal val listStringSerializer = ListSerializer(String.serializer())

internal object ScaleSerializer : KSerializer<Scale> {

    private val intArraySerializer = IntArraySerializer()
    override val descriptor = SerialDescriptor("Scale", intArraySerializer.descriptor)

    override fun deserialize(decoder: Decoder) =
        decoder.decodeSerializableValue(intArraySerializer).let { Scale(it[0] to it[1]) }

    override fun serialize(encoder: Encoder, value: Scale) =
        encoder.encodeSerializableValue(intArraySerializer, intArrayOf(value.width, value.height))

}

internal object LayoutSerializer : KSerializer<Layout> {

    override val descriptor = SerialDescriptor("Layout", listStringSerializer.descriptor)

    override fun deserialize(decoder: Decoder) = try {
        decoder as JsonDecoder
        decoder
            .decodeSerializableValue(listStringSerializer)
            .let { Layout(it) }
    } catch (e: Throwable) {
        Layout(decoder.decodeString().split("\n"))
    }

    override fun serialize(encoder: Encoder, value: Layout) =
        encoder.encodeSerializableValue(listStringSerializer, value.raw)

}

internal object PosSerializer : KSerializer<Pos> {

    private val intArraySerializer = IntArraySerializer()
    override val descriptor = SerialDescriptor("Pos", intArraySerializer.descriptor)

    override fun deserialize(decoder: Decoder) =
        decoder.decodeSerializableValue(intArraySerializer).let { Pos(it[0], it[1]) }

    override fun serialize(encoder: Encoder, value: Pos) =
        encoder.encodeSerializableValue(intArraySerializer, value.value.let { intArrayOf(it.first, it.second) })

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

    override val descriptor = buildClassSerialDescriptor("ActionKether") { element<String>("script") }

    override fun deserialize(decoder: Decoder): ActionKether {
        val element = (decoder as JsonDecoder).decodeJsonElement()
        return when (element) {
            is JsonArray -> element
                .mapNotNull { it.jsonPrimitive.contentOrNull }
                .joinToString("\\n") { it }

            is JsonPrimitive -> element.contentOrNull
            is JsonObject -> error("ActionKetherSerializer does not support JsonObject")
        }!!.let { ActionKether(it) }
    }

    override fun serialize(encoder: Encoder, value: ActionKether) {
        val scripts = value.script.split("\\n")
        scripts.singleOrNull()?.let { encoder.encodeString(it) }
            ?: encoder.encodeSerializableValue(listStringSerializer, scripts)
    }

}

internal object ScriptKetherSerializer : KSerializer<ScriptKether> {

    override val descriptor = PrimitiveSerialDescriptor("ScriptKether", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): ScriptKether {
        return ScriptKether((decoder as JsonDecoder).decodeJsonElement().jsonPrimitive.content)
    }

    override fun serialize(encoder: Encoder, value: ScriptKether) = encoder.encodeString(value.script)

}

internal object MenuTitleSerializer : JsonTransformingSerializer<MenuTitle>(MenuTitle.serializer()) {

    override fun transformDeserialize(element: JsonElement) =
        if (element !is JsonObject) {
            buildJsonObject { put("value", buildJsonArray { add(element) }) }
        } else element


    override fun transformSerialize(element: JsonElement) = element.let {
        val titles = it.jsonObject["value"]?.let { value ->
            if (value is JsonPrimitive) buildJsonArray { add(value) }
            else value as JsonArray
        }
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