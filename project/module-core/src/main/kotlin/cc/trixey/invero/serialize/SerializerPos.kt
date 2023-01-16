package cc.trixey.invero.serialize

import cc.trixey.invero.common.Pos
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.PairSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * Invero
 * cc.trixey.invero.serialize.SerializerPos
 *
 * @author Arasple
 * @since 2023/1/15 22:50
 */
@OptIn(ExperimentalSerializationApi::class)
object SerializerPos : KSerializer<Pos> {

    private val delegateSerializer = PairSerializer(Int.serializer(), Int.serializer())
    override val descriptor = SerialDescriptor("Pos", delegateSerializer.descriptor)

    override fun deserialize(decoder: Decoder): Pos {
        val pair = decoder.decodeSerializableValue(delegateSerializer)
        return Pos(pair)
    }

    override fun serialize(encoder: Encoder, value: Pos) {
        encoder.encodeSerializableValue(delegateSerializer, value.value)
    }

}