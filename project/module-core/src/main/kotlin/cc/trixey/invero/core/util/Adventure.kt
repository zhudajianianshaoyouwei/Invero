//@file:RuntimeDependencies(
//    RuntimeDependency("!net.kyori:adventure-api:4.9.3", transitive = false),
//    RuntimeDependency("!net.kyori:adventure-text-minimessage:4.1.0-SNAPSHOT", transitive = false)
//)

package cc.trixey.invero.core.util

import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.internal.parser.ParsingExceptionImpl
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer

/**
 * Invero
 * cc.trixey.invero.core.util.Adventure
 *
 * @author Arasple
 * @since 2023/1/16 18:27
 */
private const val SECTION_CHAR: Char = '§'

private const val AMPERSAND_CHAR = '&'

fun String.parseMiniMessage(): String {
    val component = try {
        MiniMessage.miniMessage().deserialize(this)
    } catch (e: ParsingExceptionImpl) {
        return translateLegacyColor().parseMiniMessage()
    }
    return LegacyComponentSerializer.legacySection().serialize(component)
}

fun String.translateAmpersandColor(): String {
    return replace(AMPERSAND_CHAR, SECTION_CHAR)
}

fun String.translateLegacyColor(): String {
    return replace(SECTION_CHAR, AMPERSAND_CHAR)
}

fun String.isPrefixColored(): Boolean {
    return startsWith(SECTION_CHAR)
}