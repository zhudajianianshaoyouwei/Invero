package cc.trixey.invero.core.command

import org.bukkit.entity.Player
import taboolib.common.platform.command.CommandBody
import taboolib.common.platform.command.subCommand

/**
 * Invero
 * cc.trixey.invero.core.command.CommandItem
 *
 * @author Arasple
 * @since 2023/2/13 18:45
 */
object CommandItem {

    @CommandBody
    val serialize = subCommand {
        execute<Player> { player, _, argument ->
        }
    }

}