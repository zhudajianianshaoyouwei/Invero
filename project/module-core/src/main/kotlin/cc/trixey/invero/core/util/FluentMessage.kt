package cc.trixey.invero.core.util

import cc.trixey.invero.common.adventure.parseMiniMessage
import org.bukkit.entity.Player
import taboolib.module.chat.ComponentText
import taboolib.module.chat.TextTransfer
import taboolib.module.chat.component
import taboolib.platform.compat.replacePlaceholder

/**
 * Invero
 * cc.trixey.invero.common.util.TextDecoration
 *
 * @author Arasple
 * @since 2023/2/22 13:06
 */

private const val SECTION_CHAR = '§'

private const val AMPERSAND_CHAR = '&'

fun String.isPrefixColored(): Boolean {
    return startsWith(SECTION_CHAR)
}

fun String.fluentMessage(): String {
    if (isBlank()) return this

    return component().build {
        transform { KetherHandler.parseInline(it, null, emptyMap()) }
        transform { it.parseMiniMessage() }
        colored()
    }.toLegacyText()
}

fun String.fluentMessage(player: Player, variables: Map<String, Any> = emptyMap()): String {
    return fluentMessageComponent(player, variables).toLegacyText()
}

fun String.fluentMessageComponent(player: Player, variables: Map<String, Any> = emptyMap()): ComponentText {
    val component = component()
    if (isBlank()) return component.build()

    return component.build {
        startsWith("&")
        // kether parser
        transform { KetherHandler.parseInline(it, player, variables) }
        // placeholder API
        transform { it.replacePlaceholder(player) }
        // miniMessage
        transform { it.parseMiniMessage() }
        // taboolib color
        colored()
        // restore def color
        defColor(this@fluentMessageComponent)
    }
}

fun TextTransfer.defColor(defString: String) {
    if (defString.startsWith(SECTION_CHAR) || defString.startsWith(AMPERSAND_CHAR)) {
        transform {
            if (it.startsWith(SECTION_CHAR)) it.removeRange(0..1)
            else it
        }
    }
}