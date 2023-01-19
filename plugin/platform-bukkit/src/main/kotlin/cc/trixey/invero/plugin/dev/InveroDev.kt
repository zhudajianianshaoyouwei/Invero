package cc.trixey.invero.plugin.dev

import cc.trixey.invero.core.InveroManager
import cc.trixey.invero.core.serialize.serializeToJson
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.command.CommandBody
import taboolib.common.platform.command.CommandHeader
import taboolib.common.platform.command.subCommand
import taboolib.common.platform.function.console

/**
 * Invero
 * cc.trixey.invero.plugin.dev.InveroDev
 *
 * @author Arasple
 * @since 2023/1/15 15:44
 */
@CommandHeader(name = "invero")
object InveroDev {

    @Awake(LifeCycle.ACTIVE)
    fun invoke() = InveroManager.load(console().cast())

    @CommandBody
    val reload = subCommand {
        execute<CommandSender> { sender, _, _ ->
            InveroManager.load(sender)
            InveroManager.getMenus().keys.forEach {
                println("Menu:: $it")
            }
        }
    }

    @CommandBody
    val open = subCommand {
        execute<Player> { player, _, argument ->
            val menuId = argument.split(" ").getOrNull(1) ?: return@execute
            InveroManager.getMenu(menuId)?.open(player)
        }
    }

    @CommandBody
    val print = subCommand {
        execute<CommandSender> { _, _, argument ->
            val menuId = argument.split(" ").getOrNull(1) ?: return@execute
            InveroManager.getMenu(menuId)?.let {
                println(it.serializeToJson())
            }
        }
    }

}