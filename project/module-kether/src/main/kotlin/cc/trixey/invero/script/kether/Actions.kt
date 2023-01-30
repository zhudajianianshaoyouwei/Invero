package cc.trixey.invero.script.kether

import cc.trixey.invero.core.util.KetherHandler
import cc.trixey.invero.library.adventure.parseAndSendMiniMessage
import org.bukkit.entity.Player
import taboolib.module.kether.KetherParser
import taboolib.module.kether.combinationParser
import taboolib.platform.compat.replacePlaceholder

/**
 * Invero
 * cc.trixey.invero.script.kether.Actions
 *
 * @author Arasple
 * @since 2023/1/24 22:51
 */
@KetherParser(["message", "msg"], namespace = "invero", shared = true)
internal fun actionMessage() = combinationParser {
    it.group(
        text(),
    ).apply(it) { message ->
        now {
            val session = session()
            if (session != null)
                session.apply { viewer.get<Player>().sendMessage(parse(message, variableAs("@context"))) }
            else {
                val player = player()
                KetherHandler
                    .parseInline(message, player, mapOf())
                    .replacePlaceholder(player)
                    .parseAndSendMiniMessage(player)
            }
        }
    }
}