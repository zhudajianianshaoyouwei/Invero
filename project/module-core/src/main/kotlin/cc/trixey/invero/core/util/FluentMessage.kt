package cc.trixey.invero.core.util

import cc.trixey.invero.common.adventure.parseMiniMessage
import org.bukkit.entity.Player
import taboolib.common.platform.function.adaptPlayer
import taboolib.module.chat.ComponentText
import taboolib.module.chat.colored
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
        transform { KetherHandler.parseInline(it, null, emptyMap()) }
        transform { it.parseMiniMessage() }
        colored()
    }.toLegacyText()
}

fun String.fluentMessage(player: Player, variables: Map<String, Any> = emptyMap()): String {
    return KetherHandler
        .parseInline(this, player, variables)
        .replacePlaceholder(player)
        .parseMiniMessage()
        .colored()
//    return fluentMessageComponent(player, variables)
//        .toLegacyText()
//        .parseMiniMessage()
//        .colored()
}

fun String.fluentMessageComponent(
    player: Player,
    variables: Map<String, Any> = emptyMap(),
    send: Boolean = false
): ComponentText {
    val component = component()
    if (isBlank()) return component.build()

    return component.build {
        startsWith("&")
        // kether parser
        transform { KetherHandler.parseInline(it, player, variables) }
        // placeholder API
        transform { it.replacePlaceholder(player) }
    }.also {
        if (send) it.sendTo(adaptPlayer(player))
    }
}