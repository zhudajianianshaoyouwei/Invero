package cc.trixey.invero.script.kether

import org.bukkit.entity.Player
import taboolib.module.kether.KetherParser
import taboolib.module.kether.combinationParser

/**
 * Invero
 * cc.trixey.invero.script.kether.Actions
 *
 * @author Arasple
 * @since 2023/1/24 22:51
 */
@KetherParser(["message"], ["msg"], namespace = "invero", shared = true)
internal fun actionMessage() = combinationParser {
    it.group(
        text(),
    ).apply(it) { message ->
        now {
            session()?.apply { viewer.get<Player>().sendMessage(parse(message)) }
        }
    }
}