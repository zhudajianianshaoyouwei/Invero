package cc.trixey.invero.core.command

import cc.trixey.invero.common.Invero
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import taboolib.common.platform.command.*

/**
 * Invero
 * cc.trixey.invero.core.command.Comamnd
 *
 * @author Arasple
 * @since 2023/1/19 11:36
 */
@CommandHeader(name = "invero", aliases = ["i"], permission = "invero.command")
object Comamnd {

    @CommandBody
    val main = mainCommand { createHelper() }

    @CommandBody
    val dev = CommandDev

    @CommandBody
    val reload = subCommand { execute { sender, _, _ -> Invero.api().getMenuManager().reload(sender) } }

    /*
    invero open <menuId>[:context] [for <player>]
     */
    @CommandBody
    val open = subCommand {
        dynamic("id") {
            suggestMenuIds()
            literal("for", optional = true) {
                dynamic("player") {
                    suggestPlayers()
                    execute<CommandSender> { _, ctx, _ ->
                        val player = ctx["player"].retrievePlayer()
                        val menu = ctx["id"].retrieveMenu()

                        if (player != null) {
                            menu?.open(player)
                        }
                    }
                }
            }
            execute<Player> { player, ctx, _ -> ctx["id"].retrieveMenu()?.open(player) }
        }
    }

}