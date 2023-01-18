package cc.trixey.invero.core.icon

import cc.trixey.invero.core.animation.CycleMode
import cc.trixey.invero.core.item.Texture
import cc.trixey.invero.core.serialize.ListSlotSerializer
import cc.trixey.invero.core.serialize.ListStringSerializer
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

/**
 * Invero
 * cc.trixey.invero.core.icon.Frame
 *
 * @author Arasple
 * @since 2023/1/16 11:51
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
class Frame(
    val delay: Long?,
    @Serializable @JsonNames("material", "mat")
    val texture: Texture?,
    val name: String?,
    @Serializable(with = ListStringSerializer::class) @JsonNames("lores")
    val lore: List<String>?,
    @Serializable @JsonNames("amt")
    val amount: Int?,
    @Serializable(with = ListSlotSerializer::class) @JsonNames("slots", "pos", "position", "positions")
    val slot: List<Slot>?,
) {

    @Serializable
    class Properties(
        @SerialName("delay") @JsonNames("default-delay", "period")
        val defaultDelay: Long = 20,
        @SerialName("mode") @JsonNames("type")
        val frameMode: CycleMode = CycleMode.LOOP
    )

}