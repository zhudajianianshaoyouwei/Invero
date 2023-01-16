package cc.trixey.invero.serialize

import cc.trixey.invero.core.action.KetherCondition
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * Invero
 * cc.trixey.invero.serialize.SerializerKetherCondition
 *
 * @author Arasple
 * @since 2023/1/16 13:13
 */
object SerializerKetherCondition : KSerializer<KetherCondition> {

    override val descriptor = PrimitiveSerialDescriptor("KetherCondition", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): KetherCondition {
        return KetherCondition(decoder.decodeString())
    }

    override fun serialize(encoder: Encoder, value: KetherCondition) {
        return encoder.encodeString(value.script)
    }

}