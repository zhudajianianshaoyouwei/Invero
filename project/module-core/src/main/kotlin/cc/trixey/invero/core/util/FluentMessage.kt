package cc.trixey.invero.core.util

import cc.trixey.invero.common.adventure.Adventure
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
        // kether parser
        transform { KetherHandler.parseInline(it, null, emptyMap()) }
        // miniMessage
        if (Adventure.isSupported) transform { Adventure.parse(it) }
        // taboo message
        colored()
    }.toLegacyText()
}

fun String.fluentMessage(player: Player, variables: Map<String, Any> = emptyMap()): String {
    return KetherHandler
        .parseInline(this, player, variables)
        .replacePlaceholder(player)
        .let { if (Adventure.isSupported) Adventure.parse(it) else it }
        .colored()

//    return fluentMessageComponent(player, variables)
//        .toLegacyText()
//        .colored()
}

fun String.fluentMessageComponent(
    player: Player,
    variables: Map<String, Any> = emptyMap(),
    send: Boolean = false
): ComponentText {
    val component = KetherHandler
        .parseInline(this, player, variables)
        .replacePlaceholder(player)
        .let { if (Adventure.isSupported) Adventure.parse(it) else it }
        .component()

    if (isBlank()) return component.build()

    return component.build { colored() }.also {
        if (send) it.sendTo(adaptPlayer(player))
    }
}