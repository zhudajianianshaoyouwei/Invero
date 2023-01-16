package cc.trixey.invero.serialize

import cc.trixey.invero.common.Scale
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.builtins.IntArraySerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import javax.management.Query.value

/**
 * Invero
 * cc.trixey.invero.serialize.SerializerScale
 *
 * @author Arasple
 * @since 2023/1/15 22:48
 */
@OptIn(ExperimentalSerializationApi::class)
object SerializerScale : KSerializer<Scale> {

    private val delegateSerializer = IntArraySerializer()
    override val descriptor = SerialDescriptor("Scale", delegateSerializer.descriptor)

    override fun deserialize(decoder: Decoder): Scale {
        val array = decoder.decodeSerializableValue(delegateSerializer)
        return Scale(array[0] to array[1])
    }

    override fun serialize(encoder: Encoder, value: Scale) {
        val data = intArrayOf(value.width, value.height)
        encoder.encodeSerializableValue(delegateSerializer, data)
    }

}