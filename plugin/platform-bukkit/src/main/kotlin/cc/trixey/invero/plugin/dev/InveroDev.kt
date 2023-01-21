package cc.trixey.invero.plugin.dev

import cc.trixey.invero.bukkit.InventoryPacket
import cc.trixey.invero.bukkit.InventoryVanilla
import cc.trixey.invero.bukkit.PanelContainer
import cc.trixey.invero.core.InveroManager
import cc.trixey.invero.core.serialize.serializeToJson
import cc.trixey.invero.core.util.KetherHandler
import cc.trixey.invero.core.util.session
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.command.CommandBody
import taboolib.common.platform.command.CommandHeader
import taboolib.common.platform.command.subCommand
import taboolib.common.platform.function.console
import taboolib.common.platform.function.submitAsync
import taboolib.platform.util.bukkitPlugin
import taboolib.platform.util.isAir
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
    val printSerailizedMenu = subCommand {
        execute<CommandSender> { _, _, argument ->
            val menuId = argument.split(" ").getOrNull(1) ?: return@execute
            InveroManager.getMenu(menuId)?.let { println(it.serializeToJson()) }
        }
    }

    @CommandBody
    val printTasks = subCommand {
        execute<CommandSender> { _, _, _ ->
            val activeWorkers = Bukkit.getScheduler().activeWorkers.count { it.owner == bukkitPlugin }
            val pendingTasks = Bukkit.getScheduler().pendingTasks.count { it.owner == bukkitPlugin }

            println(
                """
                    Tasks: (activeWorkers=$activeWorkers, pendingTasks=$pendingTasks)
                """.trimIndent()
            )
        }
    }

    @CommandBody
    val printSession = subCommand {
        execute<CommandSender> { _, _, argument ->
            val session = onlinePlayers.first().session ?: run {
                println("No session valid")
                return@execute
            }

            println(
                """
                    
                    [I][Print] ------------------------------ [SESSION]
                    Date: ${session.createdTime}
                    
                    Viewer: ${session.viewer.name}
                    Window: ${session.window.type.name}
                    Menu: ${session.menu.name}
                    Variables: ${session.variables}
                    TaskMgr: ${session.taskMgr}
                    --------------------------------------------------
                    
                """.trimIndent()
            )
        }
    }

    @CommandBody
    val printWindow = subCommand {
        execute<CommandSender> { _, _, argument ->
            val session = onlinePlayers.first().session ?: run {
                println("No session valid")
                return@execute
            }
            val window = session.window
            val inventory = window.inventory

            println(
                """
                    
                    [I][Print] ------------------------------ [WINDOW]
                    Object: $window (${window.type.name})
                    Virtualized: ${window.inventory.isVirtual()}
                    Hosted Panels: ${window.panels.size}
                    -->
                """.trimIndent()
            )

            fun dumpPanels(indent: String = " ", container: PanelContainer) {
                container.panels.forEachIndexed { index, panel ->
                    println(indent + "Panel#$index (${panel.scale} at ${panel.locate})")
                    if (panel is PanelContainer) {
                        println("$indent  > __SUB PANELS__ (${panel.panels.size})")
                        dumpPanels("$indent  ", panel)
                    }
                }
            }

            dumpPanels(container = window)

            if (inventory is InventoryPacket) {
                inventory.windowItems
            } else {
                (inventory as InventoryVanilla).container.contents
            }
                .filterNot { it.isAir }
                .joinToString(",") { it!!.type.name }
                .let {
                    println("Storage: [ $it ]")
                }
        }
    }

}