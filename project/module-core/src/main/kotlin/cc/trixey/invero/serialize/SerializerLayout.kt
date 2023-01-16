package cc.trixey.invero.serialize

import cc.trixey.invero.core.Layout
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * Invero
 * cc.trixey.invero.serialize.SerializerLayout
 *
 * @author Arasple
 * @since 2023/1/15 23:30
 */
@OptIn(ExperimentalSerializationApi::class)
object SerializerLayout : KSerializer<Layout> {

    private val delegateSerializer = ListSerializer(String.serializer())
    override val descriptor = SerialDescriptor("Layout", delegateSerializer.descriptor)

    override fun deserialize(decoder: Decoder): Layout {
        val stringList = decoder.decodeSerializableValue(delegateSerializer)
        return Layout(stringList)
    }

    override fun serialize(encoder: Encoder, value: Layout) {
        encoder.encodeSerializableValue(delegateSerializer, value.raw)
    }

}