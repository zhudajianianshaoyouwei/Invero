package cc.trixey.invero.script.kether

import cc.trixey.invero.bukkit.PanelContainer
import cc.trixey.invero.common.Panel
import cc.trixey.invero.common.panel.ElementalPanel
import cc.trixey.invero.common.util.getSiblings
import cc.trixey.invero.core.Session
import cc.trixey.invero.core.icon.IconElement
import cc.trixey.invero.core.util.session
import org.bukkit.entity.Player
import taboolib.module.kether.ScriptFrame
import taboolib.module.kether.script
import kotlin.jvm.optionals.getOrNull

/**
 * Invero
 * cc.trixey.invero.expansion.kether.Utils
 *
 * @author Arasple
 * @since 2023/1/19 17:37
 */
fun ScriptFrame.player(): Player {
    return script().sender?.castSafely<Player>() ?: error("No player found.")
}

fun ScriptFrame.getRecursivePanels(): List<Panel> {
    return session()?.window?.getPanelsRecursively() ?: listOf()
}

fun <T : Panel> ScriptFrame.findPanelAt(indexs: List<Int>): T? {
    val iterator = indexs.iterator()
    var panel: Any? = session()?.window
    while (iterator.hasNext() && panel is PanelContainer) {
        val index = iterator.next()
        panel = panel.panels[index]
    }
    @Suppress("UNCHECKED_CAST")
    return panel as T?
}

fun ScriptFrame.iconElementBy(byName: String?, bySlot: Int?): IconElement {
    return selfPanel<ElementalPanel>().findIconElement(byName, bySlot) ?: selfIcon()
}

fun ElementalPanel.findIconElement(byName: String?, bySlot: Int?): IconElement? {
    return if (byName != null) {
        elements.value.keys.find { (it as IconElement).icon.name == byName } as IconElement?
    } else if (bySlot != null) {
        val position = scale.convertToPosition(bySlot)
        elements.value.entries.find { it.value.contains(position) }?.key as IconElement?
    } else {
        null
    }
}

fun ScriptFrame.selfIcon(): IconElement {
    return variables().get<IconElement>("@icon").getOrNull() ?: error("Nulled icon context")
}

inline fun <reified T : Panel> ScriptFrame.selfPanel(): T {
    return variables().get<Panel>("@panel").getOrNull() as T? ?: error("Nulled panel context")
}

inline fun <reified T : Panel> ScriptFrame.findNearstPanel(): T? {
    val siblings = variables().get<Panel>("@panel").getOrNull()?.getSiblings()

    return (siblings ?: session()?.window?.panels)?.filterIsInstance<T>()?.firstOrNull()
}

fun ScriptFrame.session(): Session? {
    return player().session
}