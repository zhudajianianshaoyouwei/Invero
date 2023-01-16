package cc.trixey.invero.serialize

import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * Invero
 * cc.trixey.invero.serialize.ListScoping
 *
 * @author Arasple
 * @since 2023/1/16 10:14
 */
class ListScoping<T>(val serializer: KSerializer<T>) : KSerializer<List<T>> {

    private val listSerializer = ListSerializer(serializer)

    override val descriptor: SerialDescriptor = serializer.descriptor

    override fun deserialize(decoder: Decoder): List<T> = runCatching {
        decoder.decodeSerializableValue(listSerializer)
    }.getOrElse {
        listOf(decoder.decodeSerializableValue(serializer))
    }

    override fun serialize(encoder: Encoder, value: List<T>) {
        if (value.size == 1) {
            encoder.encodeSerializableValue(serializer, value.first())
        } else if (value.isNotEmpty()) {
            encoder.encodeSerializableValue(listSerializer, value)
        }
    }

}