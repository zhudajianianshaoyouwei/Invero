package cc.trixey.invero.expansion.kether

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

fun ScriptFrame.getSession(): Session {
    return getPlayer().getSession()
}