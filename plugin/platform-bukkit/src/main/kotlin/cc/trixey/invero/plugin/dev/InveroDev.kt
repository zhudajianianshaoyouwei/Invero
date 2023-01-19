package cc.trixey.invero.plugin.dev

import cc.trixey.invero.bukkit.api.InveroAPI
import cc.trixey.invero.core.InveroManager
import cc.trixey.invero.core.serialize.serializeToJson
import cc.trixey.invero.core.util.KetherHandler
import cc.trixey.invero.core.util.getSession
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.command.CommandBody
import taboolib.common.platform.command.CommandHeader
import taboolib.common.platform.command.subCommand
import taboolib.common.platform.function.console
import taboolib.common.platform.function.submitAsync
import taboolib.platform.util.onlinePlayers

/**
 * Invero
 * cc.trixey.invero.plugin.dev.InveroDev
 *
 * @author Arasple
 * @since 2023/1/15 15:44
 */
@CommandHeader(name = "invero")
object InveroDev {

    @CommandBody
    val run = subCommand {
        execute<CommandSender> { sender, _, argument ->
            val player = if (sender is Player) sender else onlinePlayers.random()
            val script = argument.removePrefix("run ")

            submitAsync {
                KetherHandler
                    .invoke(script, player, mapOf())
                    .thenApply {
                        println(
                            """
                        ------------------>
                        Script: $script
                        Result: $it
                    """.trimIndent()
                        )
                    }.get()
            }
        }
    }

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
        execute<CommandSender> { sender, _, argument ->
            if (sender is Player) {
                sender.getSession().apply {
                    println(
                        """
                            ----------------------------
                            Menu ${menu?.name}
                            ViewingWindow: ${window?.javaClass?.simpleName}
                        """.trimIndent()
                    )
                }
                return@execute
            }
            val menuId = argument.split(" ").getOrNull(1) ?: return@execute
            InveroManager.getMenu(menuId)?.let {
                println(it.serializeToJson())
            }
        }
    }

    @CommandBody
    val debugPrint = subCommand {
        execute<CommandSender> { sender, _, argument ->
            InveroAPI.bukkitManager.registeredWindows.forEach {
                println(
                    """
                        --------------- REGISTREED WINDOW: ${it.type}
                        Viewers: ${it.viewers.map { it -> it.uuid }}
                    """.trimIndent()
                )
            }
        }
    }

}