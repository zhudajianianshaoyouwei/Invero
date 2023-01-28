@file:OptIn(ExperimentalSerializationApi::class)

package cc.trixey.invero.core.icon

import cc.trixey.invero.bukkit.api.dsl.set
import cc.trixey.invero.core.AgentPanel
import cc.trixey.invero.core.InveroSettings
import cc.trixey.invero.core.Session
import cc.trixey.invero.core.compat.isPrefixColored
import cc.trixey.invero.core.panel.PanelStandard
import cc.trixey.invero.core.util.*
import kotlinx.serialization.ExperimentalSerializationApi

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
    val item = texture
        ?.generateItem(session) {
            element.value = it
            translateUpdate(session, element, this)
        }
        ?: element.value.also { generated = false }
    item.apply {
        if (generated) {
            if (name == null && original.hasName()) postName(original.getName()!!)
            if (lore.isNullOrEmpty() && original.hasLore()) postLore(original.getLore()!!)
            if (frame.amount == null && original.amount != 1) postAmount(original.amount)
        }
        val context = element.context
        if (name != null) postName(session.parse(name, context))
        if (!lore.isNullOrEmpty()) postLore(session.parse(lore, context).defaultColored())
    }

    if (amount != null) item.amount = amount
    if (slot != null) element.set(slot.flatRelease(agent.scale))

    element.value = item
}

fun Frame.translateUpdate(session: Session, element: IconElement, defaultFrame: Frame) {
    val itemStack = texture
        ?.let { if (it.isStatic()) element.value else it.generateItem(session) }
        ?: element.value

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

fun String.defaultColored() =
    if (!isPrefixColored() && isNotBlank()) "${InveroSettings.defaultNameColor}$this"
    else this