@file:Suppress("DEPRECATION")

package cc.trixey.invero.core.icon

import cc.trixey.invero.common.util.*
import cc.trixey.invero.core.AgentPanel
import cc.trixey.invero.core.Session
import cc.trixey.invero.core.item.Frame
import cc.trixey.invero.core.util.*
import cc.trixey.invero.ui.bukkit.api.dsl.set
import cc.trixey.invero.ui.bukkit.util.proceed
import kotlinx.serialization.json.contentOrNull
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import taboolib.common5.cbool
import taboolib.common5.cint
import taboolib.common5.cshort
import taboolib.module.nms.ItemTag
import taboolib.module.nms.getItemTag

/**
 * Invero
 * cc.trixey.invero.core.icon.Util
 *
 * @author Arasple
 * @since 2023/1/16 16:06
 */

fun Frame.render(agent: AgentPanel, element: IconElement) {
    val frame = this@render
    val original = element.value
    val context = element.context

    // TODO
    // support for color,enchantments
    fun ItemStack.frameApply(): ItemStack {

        name?.let {
            postName(context.parse(name).colored())
        }
        lore?.let {
            postLore(context.parse(lore).colored(enhancedLore))
        }
        damage?.let {
            durability = staticDamage ?: it.content.let { s -> context.parse(s) }.cshort
        }
        customModelData?.let {
            val model = staticCustomModelData ?: it.content.let { s -> context.parse(s) }.toIntOrNull() ?: 0
            postModel(model)
        }
        glow?.let {
            if (staticGlow == true || context.parse(it.content).cbool) {
                postShiny(context.parse(it.content).cbool ?: false)
            }
        }
        flags?.map { flag ->
            ItemFlag.values().find { it.name.equals(flag, true) }
        }?.let { flags ->
            itemMeta = itemMeta?.also { it.addItemFlags(*flags.toTypedArray()) }
        }

        unbreakable?.proceed {
            itemMeta = itemMeta?.also { it.isUnbreakable = true }
        }

        nbt?.let {
            ItemTag().apply {
                putAll(getItemTag())
                putAll(buildNBT { context.parse(it) })
                saveTo(this@frameApply)
            }
        }

        this@render.amount?.let {
            postAmount(staticAmount ?: context.parse(it.content).toIntOrNull() ?: 1)
        }

        return this
    }

    if (texture == null) {
        element.value = element.value.apply {
            frameApply()
            // 当前材质无名称，但之前有，则继承
            if (name == null && original.hasName()) postName(original.getName()!!)
            // 当前材质无Lore，但之前有，则继承
            if (lore.isNullOrEmpty() && original.hasLore()) postLore(original.getLore()!!)
            // 当前材质的数量继承
            if (frame.amount == null && original.amount != 1) postAmount(original.amount)
        }
    } else texture.generateItem(element.context) { element.value = frameApply() }

    if (slot != null) {
        element.set(slot.flatRelease(agent.scale))
    }
}

fun Frame.translateUpdate(session: Session, element: IconElement, defaultFrame: Frame) {

    fun ItemStack.update(): ItemStack {
        val basedName = name ?: defaultFrame.name
        val basedLore = lore ?: defaultFrame.lore
        val context = element.context

        if (basedName != null) postName(session.parse(basedName, context).colored())
        if (!basedLore.isNullOrEmpty()) postLore(session.parse(basedLore, context).colored(enhancedLore))

        if (staticDamage == null) damage?.let { durability = session.parse(it.content).cshort }
        if (staticAmount == null) postAmount(session.parse(this@translateUpdate.amount?.contentOrNull ?: "1").cint)
        if (staticCustomModelData == null) customModelData?.let { postModel(session.parse(it.content).cint) }
        if (staticGlow == null) glow?.let { postShiny(session.parse(it.content).cbool) }

        return this
    }

    if (texture == null || texture.isStatic()) {
        element.value = element.value.update()
    } else {
        texture.generateItem(element.context) { element.value = update() }
    }
}

fun List<String>.colored(enhanceProcess: Boolean?): List<String> {
    return if (enhanceProcess != true) {
        map { it.colored() }
    } else {
        val iterator = iterator()

        buildList {
            while (iterator.hasNext()) {
                val it = iterator.next()
                if (it.contains("\\n")) this += it.split("\\n").map { it.colored() }
                else this += it.colored()
            }
        }
    }
}

fun String.colored() =
    if (!startsWith("§") && isNotBlank()) "§7$this"
    else this