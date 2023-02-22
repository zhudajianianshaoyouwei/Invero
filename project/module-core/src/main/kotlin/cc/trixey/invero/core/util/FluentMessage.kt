package cc.trixey.invero.core.util

import cc.trixey.invero.common.adventure.parseMiniMessage
import org.bukkit.entity.Player
import taboolib.module.chat.component
import taboolib.platform.compat.replacePlaceholder

/**
 * Invero
 * cc.trixey.invero.common.util.TextDecoration
 *
 * @author Arasple
 * @since 2023/2/22 13:06
 */
fun String.fluentMessage(): String {
    if (isBlank()) return this

    return component().build {
        // kether parser
        transform { KetherHandler.parseInline(it, null, emptyMap()) }
        // placeholder API - not supported
        // miniMessage
        transform { parseMiniMessage() }
        // taboolib color
        colored()
    }.toLegacyText()
}

fun String.fluentMessage(player: Player, variables: Map<String, Any> = emptyMap()): String {
    if (isBlank()) return this

    return component().build {
        // kether parser
        transform { KetherHandler.parseInline(it, player, variables) }
        // placeholder API
        transform { replacePlaceholder(player) }
        // miniMessage
        transform { parseMiniMessage() }
        // taboolib color
        colored()
    }.toLegacyText()
}
