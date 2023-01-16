package cc.trixey.invero.core

import cc.trixey.invero.core.animation.CycleMode
import cc.trixey.invero.core.animation.CycleString
import cc.trixey.invero.serialize.ListScoping
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.encoding.*

/**
 * Invero
 * cc.trixey.invero.core.MenuTitle
 *
 * @author Arasple
 * @since 2023/1/15 19:53
 */
@Serializable
class MenuTitle(
    val value: List<String>,
    val period: Int? = null,
    val mode: CycleMode? = null
) {

    constructor(value: String) : this(listOf(value))

    fun use(block: (default: String, value: CycleString?) -> Unit) {
        return block(
            value.first(),
            if (isSingle()) null else CycleString(value.toTypedArray())
        )
    }

    fun isSingle(): Boolean {
        return value.size <= 1 || period == null || mode == null || period < 0
    }

    fun getDefault(): String {
        return value.getOrElse(0) { "Untitled" }
    }

    @OptIn(ExperimentalSerializationApi::class)
    @Serializer(forClass = MenuTitle::class)
    companion object : KSerializer<MenuTitle> {

        override fun serialize(encoder: Encoder, value: MenuTitle) {
            if (value.value.size == 1) {
                encoder.encodeString(value.value.first())
            } else {
                encoder.encodeStructure(descriptor) {
                    encodeSerializableElement(descriptor, 0, ListScoping(String.serializer()), value.value)
                    if (value.period != null && value.period > 0) encodeIntElement(descriptor, 1, value.period)
                    if (value.mode != null) encodeSerializableElement(descriptor, 2, CycleMode.serializer(), value.mode)
                }
            }
        }

        override fun deserialize(decoder: Decoder) = runCatching {
            decoder.decodeStructure(descriptor) {
                var value: List<String>? = null
                var period: Int = -1
                var mode = CycleMode.LOOP

                loop@ while (true) {
                    when (val i = decodeElementIndex(descriptor)) {
                        CompositeDecoder.DECODE_DONE -> break@loop
                        0 -> value = decodeSerializableElement(descriptor, i, ListScoping(String.serializer()))
                        1 -> period = decodeIntElement(descriptor, i)
                        2 -> mode = decodeSerializableElement(descriptor, i, CycleMode.serializer())
                    }
                }
                MenuTitle(value!!, period, mode)
            }
        }.getOrElse {

            runCatching {
                MenuTitle(decoder.decodeString())
            }.getOrElse { it2 ->
                it.printStackTrace()
                it2.printStackTrace()
            } as MenuTitle
        }

    }

}
