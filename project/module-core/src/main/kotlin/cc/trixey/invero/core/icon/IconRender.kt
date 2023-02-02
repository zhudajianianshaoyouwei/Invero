@file:Suppress("DEPRECATION")

package cc.trixey.invero.core.icon

import cc.trixey.invero.common.adventure.isPrefixColored
import cc.trixey.invero.common.api.InveroSettings
import cc.trixey.invero.common.supplier.Object
import cc.trixey.invero.common.util.*
import cc.trixey.invero.core.AgentPanel
import cc.trixey.invero.core.Session
import cc.trixey.invero.core.item.Frame
import cc.trixey.invero.core.util.*
import cc.trixey.invero.ui.bukkit.api.dsl.set
import cc.trixey.invero.ui.bukkit.util.proceed
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import taboolib.module.nms.ItemTag
import taboolib.module.nms.getItemTag

/**
 * Invero
 * cc.trixey.invero.core.icon.Util
 *
 * @author Arasple
 * @since 2023/1/16 16:06
 */

fun Frame.render(session: Session, agent: AgentPanel, element: IconElement) {
    val frame = this@render
    val original = element.value

    if (texture == null) {
        element.value = element.value.apply {
            // 当前材质无名称，但之前有，则继承
            if (name == null && original.hasName()) postName(original.getName()!!)
            // 当前材质无Lore，但之前有，则继承
            if (lore.isNullOrEmpty() && original.hasLore()) postLore(original.getLore()!!)
            // 当前材质的数量继承
            if (frame.amount == null && original.amount != 1) postAmount(original.amount)
        }
    } else texture.generateItem(element.context) {
        val context = element.context
        name?.let { postName(context.parse(name)) }
        lore?.let { postLore(context.parse(lore).defaultColored()) }
        damage?.let { durability = it }
        customModelData?.let { postModel(it) }
        glow?.proceed {
            addEnchantment(Enchantment.values().random(), 1)
            itemMeta = itemMeta?.also { it.addItemFlags(ItemFlag.HIDE_ENCHANTS) }
        }
        flags?.map { flag ->
            ItemFlag.values().find { it.name.equals(flag, true) }
        }?.let { flags ->
            itemMeta = itemMeta?.also { it.addItemFlags(*flags.toTypedArray()) }
        }
        unbreakable?.proceed { itemMeta = itemMeta?.also { it.isUnbreakable = true } }
        nbtData?.let {
            val raw = if (nbtDataDynamic) ItemTag.fromLegacyJson(session.parse(nbtData.toJson(), context)) else nbtData

            ItemTag().apply {
                putAll(getItemTag())
                putAll(raw)
                saveTo(this@generateItem)
            }
        }
        // TODO
        // support for color,enchantments
        if (this@render.amount != null) amount = this@render.amount
        element.value = this
    }

    if (slot != null) element.set(slot.flatRelease(agent.scale))
}

fun Frame.translateUpdate(session: Session, element: IconElement, defaultFrame: Frame) {

    fun ItemStack.update(): ItemStack {
        val basedName = name ?: defaultFrame.name
        val basedLore = lore ?: defaultFrame.lore
        val context = element.context

        if (basedName != null) postName(session.parse(basedName, context).defaultColored())
        if (!basedLore.isNullOrEmpty()) postLore(session.parse(basedLore, context).defaultColored())

        return this
    }

    if (texture == null || texture.isStatic()) {
        element.value = element.value.update()
    } else {
        texture.generateItem(element.context) { element.value = update() }
    }
}

fun List<String>.defaultColored() = map {
    if (!it.isPrefixColored() && it.isNotBlank()) "${InveroSettings.defaultLoreColor}$it"
    else it
}

fun String.defaultColored() =
    if (!isPrefixColored() && isNotBlank()) "${InveroSettings.defaultNameColor}$this"
    else this