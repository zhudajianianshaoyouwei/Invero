package cc.trixey.invero.core.item

import cc.trixey.invero.core.animation.CycleMode
import cc.trixey.invero.core.icon.Slot
import cc.trixey.invero.core.serialize.ListSlotSerializer
import cc.trixey.invero.core.serialize.ListStringSerializer
import cc.trixey.invero.core.util.containsAnyPlaceholder
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.json.JsonNames
import taboolib.module.nms.ItemTag
import taboolib.module.nms.ItemTagData

/**
 * Invero
 * cc.trixey.invero.core.item.Frame
 *
 * @author Arasple
 * @since 2023/1/16 11:51
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
class Frame(
    val delay: Long?,
    @Serializable
    @JsonNames("material", "mat")
    val texture: Texture?,
    val name: String?,
    @Serializable(with = ListStringSerializer::class)
    @JsonNames("lores")
    val lore: List<String>?,
    @Serializable @JsonNames("count", "amt")
    val amount: Int?,
    val damage: Short?,
    @JsonNames("model")
    val customModelData: Int?,
    val color: String?,
    val glow: Boolean?,
    val enchantments: Map<String, Int>?,
    @Serializable(with = ListStringSerializer::class)
    val flags: List<String>?,
    val unbreakable: Boolean?,
    val nbt: Map<String, String>?,
    @Serializable(with = ListSlotSerializer::class)
    @JsonNames("slots", "pos", "position", "positions")
    val slot: List<Slot>?,
) {

    @Transient
    val nbtData = run {
        if (nbt == null) null
        else {
            val tag = ItemTag()
            nbt.forEach { (key, value) -> tag[key] = ItemTagData.toNBT(value) }
            tag
        }
    }

    @Transient
    val nbtDataDynamic = nbtData?.toJson()?.containsAnyPlaceholder() ?: false

    /*
    TODO properties support:

     - banner
     */

    @Serializable
    class Properties(
        @SerialName("delay")
        @JsonNames("default-delay", "period")
        val defaultDelay: Long = 20,
        @SerialName("mode")
        @JsonNames("type")
        val frameMode: CycleMode = CycleMode.LOOP
    )

}