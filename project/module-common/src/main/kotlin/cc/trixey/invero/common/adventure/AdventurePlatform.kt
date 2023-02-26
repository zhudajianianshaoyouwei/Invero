package cc.trixey.invero.common.adventure

import net.kyori.adventure.platform.bukkit.BukkitAudiences
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.bukkit.entity.Player
import taboolib.module.nms.MinecraftVersion
import taboolib.platform.util.bukkitPlugin

/**
 * Invero
 * cc.trixey.invero.common.AdventurePlatform
 *
 * @author Arasple
 * @since 2023/1/16 18:27
 */
class AdventurePlatform {

    private val bukkitAudiences by lazy {
        BukkitAudiences.create(bukkitPlugin)
    }

    private val legacyComponentSerializer by lazy {
        LegacyComponentSerializer.builder().apply {
            if (MinecraftVersion.majorLegacy >= 11600) {
                hexColors()
                useUnusualXRepeatedCharacterHexFormat()
            }
        }.build()
    }


    fun parseMiniMessage(string: String): String {
        val component = parseMiniMessageComponent(string.translateLegacyColor())
        return legacyComponentSerializer.serialize(component)
    }

    fun parseMiniMessageComponent(string: String): Component {
        return MiniMessage.miniMessage().deserialize(string)
    }

    fun parseAndSendMiniMessage(string: String, player: Player) {
        bukkitAudiences.player(player).sendMessage(parseMiniMessageComponent(string.translateAmpersandColor()))
    }

    companion object {

        private const val SECTION_CHAR = '§'

        private const val AMPERSAND_CHAR = '&'

        fun String.translateAmpersandColor(): String {
            return replace(Companion.AMPERSAND_CHAR, Companion.SECTION_CHAR)
        }

        fun String.translateLegacyColor(): String {
            return replace(Companion.SECTION_CHAR, Companion.AMPERSAND_CHAR)
        }

    }


}