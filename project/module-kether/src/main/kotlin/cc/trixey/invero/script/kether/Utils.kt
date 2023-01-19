package cc.trixey.invero.script.kether

import cc.trixey.invero.common.Panel
import cc.trixey.invero.common.panel.PanelContainer
import cc.trixey.invero.common.util.getSiblings
import cc.trixey.invero.core.Session
import cc.trixey.invero.core.util.getSession
import org.bukkit.entity.Player
import taboolib.module.kether.ScriptFrame
import taboolib.module.kether.script

/**
 * Invero
 * cc.trixey.invero.expansion.kether.Utils
 *
 * @author Arasple
 * @since 2023/1/19 17:37
 */

fun ScriptFrame.getPlayer(): Player {
    return script().sender?.castSafely<Player>() ?: error("No player found.")
}

fun ScriptFrame.getRecursivePanels(): List<Panel> {
    return getSession().window?.getPanelsRecursively() ?: listOf()
}

fun <T : Panel> ScriptFrame.findPanelAt(indexs: List<Int>): T? {
    val iterator = indexs.iterator()
    var panel: Any? = getSession().window

    while (iterator.hasNext() && panel is PanelContainer) {
        val index = iterator.next()
        panel = panel.panels[index]
    }
    @Suppress("UNCHECKED_CAST")
    return panel as T?
}

inline fun <reified T : Panel> ScriptFrame.findNearstPanel(): T? {
    return variables().get<Panel>("@panel").get().getSiblings().filterIsInstance<T>().firstOrNull()
}

fun ScriptFrame.getSession(): Session {
    return getPlayer().getSession()
}