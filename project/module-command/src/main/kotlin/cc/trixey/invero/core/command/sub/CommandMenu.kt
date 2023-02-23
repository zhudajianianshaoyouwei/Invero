package cc.trixey.invero.core.command.sub

import cc.trixey.invero.common.Invero
import cc.trixey.invero.common.util.PasteResult.Status.ERROR
import cc.trixey.invero.common.util.PasteResult.Status.SUCCESS
import cc.trixey.invero.common.util.createContent
import cc.trixey.invero.common.util.paste
import cc.trixey.invero.core.command.createHelper
import cc.trixey.invero.core.command.menu
import cc.trixey.invero.core.command.player
import cc.trixey.invero.core.command.suggestMenuIds
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import taboolib.common.platform.command.*
import taboolib.common.platform.function.submitAsync
import taboolib.platform.util.sendLang
import java.util.concurrent.TimeUnit

/**
 * Invero
 * cc.trixey.invero.core.command.sub.CommandMenu
 *
 * @author Arasple
 * @since 2023/2/18 13:03
 */
@CommandHeader(name = "menu")
object CommandMenu {

    @CommandBody
    val main = mainCommand { createHelper() }

    @CommandBody
    val reload = subCommand { execute { sender, _, _ -> Invero.API.getMenuManager().reload(sender) } }

    /*
    list [filter]
     */
    @CommandBody
    val list = subCommand {
        dynamic("filter") {

        }
        // TODO
        execute<CommandSender> { sender, _, _ ->
            sender.sendMessage("呜呜呜还没写")
        }
    }

    /*
    dump [id]
     */
    @CommandBody
    val dump = subCommand {
        dynamic("menu") {
            suggestMenuIds()
            execute<Player> { player, ctx, _ ->
                val menu = ctx.menu ?: return@execute
                submitAsync {
                    val serialized = Invero.API.getMenuManager().serializeToJson(menu)
                    player.sendLang("paste-init")

                    paste(
                        "Invero Menu Serialization",
                        "menu serialized as json",
                        48,
                        TimeUnit.HOURS,
                        createContent("${menu.id}", serialized, "JSON"),
                    ).apply {
                        when (status) {
                            SUCCESS -> player.sendLang("paste-success", anonymousLink)
                            ERROR -> player.sendLang("paste-failed")
                        }
                    }
                }
            }
        }
    }

    /*
    open <menu> [player] with []
     */
    @CommandBody
    val open = subCommand {
        dynamic("menu") {
            suggestMenuIds()

            dynamic("player", optional = true) {
                suggestPlayers()
                execute<CommandSender> { _, ctx, _ -> ctx.player?.let { ctx.menu?.open(it) } }
            }

            literal("with", optional = true) {
                dynamic("args") {
                    suggestPlayers()
                    execute<CommandSender> { _, ctx, args ->
                        val player = ctx.player ?: ctx.player().cast()
                        args.removePrefix("with ")
                        // TODO args
                        player.let { ctx.menu?.open(it) }
                    }
                }
            }

            execute<Player> { player, ctx, _ -> ctx.menu?.open(player) }
        }
    }

}