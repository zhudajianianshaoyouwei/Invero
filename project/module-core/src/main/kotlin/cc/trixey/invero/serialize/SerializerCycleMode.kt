package cc.trixey.invero.serialize

import cc.trixey.invero.core.animation.CycleMode
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * Invero
 * cc.trixey.invero.serialize.SerializerCycleMode
 *
 * @author Arasple
 * @since 2023/1/16 12:07
 */
object SerializerCycleMode : KSerializer<CycleMode> {

    override val descriptor = PrimitiveSerialDescriptor("CycleMode", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: CycleMode) {
        encoder.encodeString(value.name)
    }


    override fun deserialize(decoder: Decoder): CycleMode {
        return CycleMode.of(decoder.decodeString())
    }

}