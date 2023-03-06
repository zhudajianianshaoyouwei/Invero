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

/**
 * 常规文本格式替换
 * 依次翻译
 * - Kether Inline
 * - PlaceholderAPI
 * - TabooLib Colored
 * - MiniMessage Parse (if supported)
 */
fun String.translateFormattedMessage(player: Player, variables: Map<String, Any> = emptyMap()) =
    KetherHandler
        .parseInline(this, player, variables)
        .replacePlaceholder(player)
        .colored()
        .let { if (Adventure.isSupported) Adventure.parse(it) else it }

/**
 * （默认）发送格式化消息
 * 依次翻译
 * - Kether Inline
 * - Placeholder API
 * - TabooLib Colored
 * - MiniMessage Component Send (if supported) (else send noraml message)
 */
fun String.sendFormattedComponent(player: Player, variables: Map<String, Any> = emptyMap()) =
    KetherHandler
        .parseInline(this, player, variables)
        .replacePlaceholder(player)
        .colored()
        .let {
            if (Adventure.isSupported) {
                Adventure.parseAndSend(it, player)
            } else {
                player.sendMessage(it)
            }
        }

/**
 * 发送 TabooLib ComponentText
 * 依次翻译
 * - Kether Inline
 * - Placeholder API
 * - TabooLib Component (with colored)
 */
fun String.sendFormattedTabooComponent(player: Player, variables: Map<String, Any> = emptyMap()) =
    KetherHandler
        .parseInline(this, player, variables)
        .replacePlaceholder(player)
        .colored()
        .component()
        .build()
        .sendTo(adaptPlayer(player))