package cc.trixey.invero.script.kether

import cc.trixey.invero.bukkit.PanelContainer
import cc.trixey.invero.common.Panel
import cc.trixey.invero.common.util.getSiblings
import cc.trixey.invero.core.Session
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

inline fun <reified T : Panel> ScriptFrame.findNearstPanel(): T? {
    val siblings = variables().get<Panel>("@panel").getOrNull()?.getSiblings()

    return (siblings ?: session()?.window?.panels)?.filterIsInstance<T>()?.firstOrNull()
}

fun ScriptFrame.session(): Session? {
    return player().session
}