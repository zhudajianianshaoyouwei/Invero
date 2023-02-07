package cc.trixey.invero.core.item

import cc.trixey.invero.common.util.postAmount
import cc.trixey.invero.common.util.postLore
import cc.trixey.invero.common.util.postName
import cc.trixey.invero.core.Context
import cc.trixey.invero.core.icon.Slot
import cc.trixey.invero.core.serialize.ListSlotSerializer
import cc.trixey.invero.core.serialize.ListStringSerializer
import cc.trixey.invero.core.util.containsAnyPlaceholder
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.json.JsonNames
import org.bukkit.inventory.ItemStack
import taboolib.library.reflex.Reflex.Companion.getProperty
import taboolib.library.reflex.Reflex.Companion.setProperty
import taboolib.module.nms.ItemTag
import taboolib.module.nms.ItemTagData

/**
 * Invero
 * cc.trixey.invero.core.item.WindowFrame
 *
 * @author Arasple
 * @since 2023/1/16 11:51
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
class Frame(
    @JsonNames("last")
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

    init {
        // yaml |- 多行写法
        if (lore != null && lore.size == 1) {
            setProperty("lore", lore.single().split("\n"))
        }
    }

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

    fun generateItem(context: Context, callback: (ItemStack) -> Unit) = texture?.generateItem(context) {
        val meta = itemMeta
        name?.let { postName(context.parse(it)) }
        lore?.let { postLore(context.parse(lore)) }
        damage?.let { durability = it }
        customModelData?.let { meta?.setCustomModelData(it) }
        // No more properties are supported
        postAmount(amount)
        itemMeta = meta

        callback(this)
    }

    fun inheirt(frame: Frame) = arrayOf(
        "texture",
        "name",
        "lore",
        "amount",
        "damage",
        "customModelData",
        "color",
        "glow",
        "enchantments",
        "flags",
        "unbreakable",
        "nbt",
        "slot"
    ).forEach {
        if (getProperty<Any?>(it) == null) {
            val copy = frame.getProperty<Any>(it)
            if (copy is Texture) setProperty(it, copy.clone())
            else setProperty(it, copy)
        }
    }


}