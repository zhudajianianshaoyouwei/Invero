@file:OptIn(ExperimentalSerializationApi::class)
@file:Suppress("DEPRECATION")

package cc.trixey.invero.core.icon

import cc.trixey.invero.bukkit.api.dsl.set
import cc.trixey.invero.bukkit.util.proceed
import cc.trixey.invero.core.AgentPanel
import cc.trixey.invero.core.InveroSettings
import cc.trixey.invero.core.Session
import cc.trixey.invero.core.compat.isPrefixColored
import cc.trixey.invero.core.panel.PanelStandard
import cc.trixey.invero.core.util.*
import kotlinx.serialization.ExperimentalSerializationApi
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
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
    var generated = true
    val item = texture?.generateItem(session) {
        element.value = it
        translateUpdate(session, element, this)
    } ?: element.value.also { generated = false }
    item.apply {
        // 生成的新物品，自动继承之前物品丢失的相关属性
        if (generated) {
            // 当前材质无名称，但之前有，则继承
            if (name == null && original.hasName()) postName(original.getName()!!)
            // 当前材质无Lore，但之前有，则继承
            if (lore.isNullOrEmpty() && original.hasLore()) postLore(original.getLore()!!)
            // 当前材质的数量继承
            if (frame.amount == null && original.amount != 1) postAmount(original.amount)
        }
        val context = element.context
        name?.let { postName(session.parse(name, context)) }
        lore?.let { postLore(session.parse(lore, context).defaultColored()) }
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
                putAll(item.getItemTag())
                putAll(raw)
                saveTo(item)
            }
        }
        // TODO
        // support for color,enchantments
    }

    if (amount != null) item.amount = amount
    if (slot != null) element.set(slot.flatRelease(agent.scale))

    element.value = item
}

fun Frame.translateUpdate(session: Session, element: IconElement, defaultFrame: Frame) {
    val itemStack = texture?.let { if (it.isStatic()) element.value else it.generateItem(session) } ?: element.value

    itemStack.apply {
        val basedName = name ?: defaultFrame.name
        val basedLore = lore ?: defaultFrame.lore
        val context = element.context

        if (basedName != null) postName(session.parse(basedName, context))
        if (!basedLore.isNullOrEmpty()) postLore(session.parse(basedLore, context).defaultColored())
    }

    element.value = itemStack
}

fun Icon.getValidId(agentPanel: AgentPanel) = when {
    id != null -> id
    agentPanel is PanelStandard -> agentPanel.icons.entries.find { it.value == this }?.key
    else -> null
}

fun List<String>.defaultColored() = map {
    if (!it.isPrefixColored() && it.isNotBlank()) "${InveroSettings.defaultLoreColor}$it"
    else it
}

fun String.defaultColored() = if (!isPrefixColored() && isNotBlank()) "${InveroSettings.defaultNameColor}$this"
else this