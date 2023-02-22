package cc.trixey.invero.common.adventure

import net.kyori.adventure.platform.bukkit.BukkitAudiences
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.internal.parser.ParsingExceptionImpl
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.bukkit.entity.Player
import taboolib.module.nms.MinecraftVersion
import taboolib.platform.util.bukkitPlugin

/**
 * Invero
 * cc.trixey.invero.common.Adventure
 *
 * @author Arasple
 * @since 2023/1/16 18:27
 */
private const val SECTION_CHAR = '§'

private const val AMPERSAND_CHAR = '&'

private val bukkitAudiences by lazy { BukkitAudiences.create(bukkitPlugin) }

private val legacyComponentSerializer by lazy {
    LegacyComponentSerializer.builder().apply {
        if (MinecraftVersion.majorLegacy >= 11600) {
            hexColors()
            useUnusualXRepeatedCharacterHexFormat()
        }
    }.build()
}


fun String.parseMiniMessage(): String {
    val component = try {
        parseMiniMessageComponent()
    } catch (e: ParsingExceptionImpl) {
        return translateLegacyColor().parseMiniMessage()
    }
    return legacyComponentSerializer.serialize(component)
}

fun String.parseMiniMessageComponent(): Component {
    return MiniMessage.miniMessage().deserialize(this)
}

fun String.parseAndSendMiniMessage(player: Player) {
    bukkitAudiences.player(player).sendMessage(translateAmpersandColor().parseMiniMessageComponent())
}

fun String.translateAmpersandColor(): String {
    return replace(AMPERSAND_CHAR, SECTION_CHAR)
}

fun String.translateLegacyColor(): String {
    return replace(SECTION_CHAR, AMPERSAND_CHAR)
}

