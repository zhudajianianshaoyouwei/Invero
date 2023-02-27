package cc.trixey.invero.core.util

import cc.trixey.invero.common.adventure.Adventure
import org.bukkit.entity.Player
import taboolib.common.platform.function.adaptPlayer
import taboolib.module.chat.ComponentText
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
    return KetherHandler
        .parseInline(this, null, emptyMap())
        .let { if (Adventure.isSupported) Adventure.parse(it) else it }
        .component()
        .build { colored() }
        .toLegacyText()
}

fun String.fluentMessage(player: Player, variables: Map<String, Any> = emptyMap()): String {
    return fluentMessageComponent(player, variables).toLegacyText()
}

fun String.fluentMessageComponent(
    player: Player,
    variables: Map<String, Any> = emptyMap(),
    send: Boolean = false
): ComponentText {
    // 如果为空则直接返回
    if (isBlank()) return component().build()
    // 依次解析 Kether Inline，PlaceholderAPI 和 MiniMessage
    // 最后转化为 module-chat 提供的 ComponentText
    val component = KetherHandler
        .parseInline(this, player, variables)
        .replacePlaceholder(player)
        .let { if (Adventure.isSupported) Adventure.parse(it) else it }
        .component()

    return component
        .build { colored() }
        .also { if (send) it.sendTo(adaptPlayer(player)) }
}