package cc.trixey.invero.serialize

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.bukkit.event.inventory.InventoryType

/**
 * Invero
 * cc.trixey.invero.serialize.SerializerInventoryType
 *
 * @author Arasple
 * @since 2023/1/15 22:08
 */
object SerializerInventoryType : KSerializer<InventoryType> {

    override val descriptor = PrimitiveSerialDescriptor("InventoryType", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): InventoryType {
        val input = decoder.decodeString().uppercase().replace(' ', '_').replace('-', '_')
        return InventoryType.values().find { it.name == input } ?: error("Unknown inventory type: $input")
    }

    override fun serialize(encoder: Encoder, value: InventoryType) {
        encoder.encodeString(value.name)
    }

}
